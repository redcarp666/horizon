package org.redcarp.core.util;

import cn.hutool.core.util.StrUtil;
import org.redcarp.core.exception.HorizonRuntimeException;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;


public class ClassesUtils {
	public static final Map<String, Class<?>> NAME_PRIMITIVE_MAP = MapUtils.immutableMapOf("boolean",
	                                                                                       Boolean.TYPE,
	                                                                                       "byte",
	                                                                                       Byte.TYPE,
	                                                                                       "char",
	                                                                                       Character.TYPE,
	                                                                                       "short",
	                                                                                       Short.TYPE,
	                                                                                       "int",
	                                                                                       Integer.TYPE,
	                                                                                       "long",
	                                                                                       Long.TYPE,
	                                                                                       "double",
	                                                                                       Double.TYPE,
	                                                                                       "float",
	                                                                                       Float.TYPE,
	                                                                                       "void",
	                                                                                       Void.TYPE);
	public static final String CGLIB_CLASS_SEPARATOR = "$$";
	public static final char PACKAGE_SEPARATOR_CHAR = '.';
	public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

	private ClassesUtils() {
	}


	public static Class<?> asClass(final ClassLoader classLoader, final String className) throws ClassNotFoundException {
		return asClass(classLoader, className, true);
	}


	public static Class<?> asClass(final ClassLoader classLoader, final String className, final boolean initialize) throws ClassNotFoundException {
		return NAME_PRIMITIVE_MAP.getOrDefault(className, Class.forName(className, initialize, classLoader));
	}


	public static Class<?> asClass(String className) {
		try {
			return asClass(defaultClassLoader(), className, true);
		} catch (ClassNotFoundException e) {
			throw new HorizonRuntimeException(e);
		}
	}

	public static void checkPackageAccess(Class<?> clazz) {
		checkPackageAccess(clazz.getName());
		if (Proxy.isProxyClass(clazz)) {
			checkProxyPackageAccess(clazz);
		}
	}

	public static void checkPackageAccess(String name) {
		SecurityManager s = System.getSecurityManager();
		if (s != null) {
			String cname = name.replace('/', PACKAGE_SEPARATOR_CHAR);
			if (!cname.isEmpty() && cname.charAt(0) == '[') {
				int b = cname.lastIndexOf('[') + 2;
				if (b > 1 && b < cname.length()) {
					cname = cname.substring(b);
				}
			}
			int i = cname.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
			if (i != -1) {
				s.checkPackageAccess(cname.substring(0, i));
			}
		}
	}

	public static void checkProxyPackageAccess(Class<?> clazz) {
		SecurityManager s = System.getSecurityManager();
		if (s != null && Proxy.isProxyClass(clazz)) {
			// check proxy interfaces if the given class is a proxy class
			for (Class<?> intf : clazz.getInterfaces()) {
				checkPackageAccess(intf);
			}
		}
	}

	public static ClassLoader defaultClassLoader() {
		ClassLoader classLoader = FunctionUtils.trySupplied(Thread.currentThread()::getContextClassLoader);
		if (classLoader == null) {
			classLoader = ClassesUtils.class.getClassLoader();
			if (classLoader == null) {
				classLoader = FunctionUtils.trySupplied(ClassLoader::getSystemClassLoader);
			}
		}
		return classLoader;
	}


	public static Set<Class<?>> getAllInterfaces(final Class<?> clazz) {
		AssertionUtils.shouldNotNull(clazz, "Can't get interfaces from null class!");
		Set<Class<?>> interfaces = new LinkedHashSet<>();
		Class<?> usedClazz = clazz.isArray() ? clazz.getComponentType() : clazz;
		if (usedClazz.isInterface()) {
			interfaces.add(usedClazz);
		}
		Class<?> current = usedClazz;
		while (current != null) {
			Class<?>[] ifcs = current.getInterfaces();
			for (Class<?> ifc : ifcs) {
				interfaces.addAll(getAllInterfaces(ifc));
			}
			current = current.getSuperclass();
		}
		return interfaces;
	}


	public static Set<Class<?>> getAllInterfaces(final Object object) {
		return getAllInterfaces(AssertionUtils.shouldNotNull(object,
		                                                     "Can't get interfaces from null object!").getClass());
	}


	public static Set<Class<?>> getAllSuperClasses(final Class<?> clazz) {
		AssertionUtils.shouldNotNull(clazz, "Can't get super classes from null class!");
		Set<Class<?>> superClasses = new LinkedHashSet<>();
		Class<?> usedClazz = clazz.isArray() ? clazz.getComponentType() : clazz;
		if (!usedClazz.isInterface() && usedClazz != Object.class) {
			Class<?> superClass = usedClazz.getSuperclass();
			while (superClass != null) {
				superClasses.add(superClass);
				superClass = superClass.getSuperclass();
			}
		}
		return superClasses;
	}

	public static Set<Class<?>> getAllSuperClasses(final Object object) {
		return getAllSuperClasses(AssertionUtils.shouldNotNull(object,
		                                                       "Can't get super classes from null object!").getClass());
	}

	public static Set<Class<?>> getAllSuperclassesAndInterfaces(final Class<?> clazz) {
		Set<Class<?>> set = new LinkedHashSet<>(getAllSuperClasses(clazz));
		set.addAll(getAllInterfaces(clazz));
		return set;
	}

	public static Class<?> getClass(Type type) {
		if (type instanceof Class<?>) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType) type).getRawType());
		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null) {
				return Array.newInstance(componentClass, 0).getClass();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static <T> Constructor<? extends T> getDeclaredConstructor(Class<T> clazz, Class<?>... paramTypes) throws NoSuchMethodException {
		if (System.getSecurityManager() == null) {
			return clazz.getDeclaredConstructor(paramTypes);
		} else {
			try {
				return AccessController.doPrivileged((PrivilegedExceptionAction<Constructor<? extends T>>) () -> {
					Constructor<? extends T> constructor = null;
					try {
						constructor = clazz.getDeclaredConstructor(paramTypes);
					} catch (SecurityException ex) {
						// Noop!
					}
					return constructor;
				});
			} catch (PrivilegedActionException e) {
				Exception ex = e.getException();
				if (ex instanceof NoSuchMethodException) {
					throw (NoSuchMethodException) ex;
				} else {
					throw ThrowableUtils.asUncheckedException(ex);
				}
			}
		}
	}

	public static String getPackageName(String fullClassName) {
		if (EmptyUtils.isEmpty(fullClassName)) {
			return StrUtil.EMPTY;
		}
		int lastDot = fullClassName.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
		return lastDot < 0 ? StrUtil.EMPTY : fullClassName.substring(0, lastDot);
	}

	public static String getShortClassName(String className) {
		String shortClassName = className;
		if (EmptyUtils.isEmpty(shortClassName)) {
			return StrUtil.EMPTY;
		}
		final StringBuilder arrayPrefix = new StringBuilder();
		if (!shortClassName.isEmpty() && shortClassName.charAt(0) == '[') {
			while (shortClassName.charAt(0) == '[') {
				shortClassName = shortClassName.substring(1);
				arrayPrefix.append("[]");
			}
			if (shortClassName.charAt(0) == 'L' && shortClassName.charAt(shortClassName.length() - 1) == ';') {
				shortClassName = shortClassName.substring(1, shortClassName.length() - 1);
			}
		}
		final int lastDotIdx = shortClassName.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
		final int innerIdx = shortClassName.indexOf(INNER_CLASS_SEPARATOR_CHAR, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
		String out = shortClassName.substring(lastDotIdx + 1);
		if (innerIdx != -1) {
			out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
		}
		return out + arrayPrefix;
	}

	public static Class<?> getUserClass(Class<?> clazz) {
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superclass = clazz.getSuperclass();
			if (superclass != null && Object.class != superclass) {
				return superclass;
			}
		}
		return clazz;
	}

	public static Class<?> getUserClass(Object instance) {
		return getUserClass(instance.getClass());
	}

	public static boolean isConcrete(Class<?> clazz) {
		return clazz != null && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers());
	}

	public static boolean isEnum(Class<?> clazz) {
		return clazz != null && (clazz.isEnum() || clazz.isArray() && clazz.getComponentType().isEnum());
	}

	public static void traverseAllInterfaces(final Class<?> clazz, Function<Class<?>, Boolean> visitor) {
		if (clazz != null) {
			Class<?> usedClazz = clazz.isArray() ? clazz.getComponentType() : clazz;
			if (usedClazz.isInterface() && !visitor.apply(usedClazz)) {
				return;
			}
			Class<?> current = usedClazz;
			stop:
			while (current != null) {
				Class<?>[] ifcs = current.getInterfaces();
				for (Class<?> ifc : ifcs) {
					for (Class<?> cls : getAllInterfaces(ifc)) {
						if (!visitor.apply(cls)) {
							break stop;
						}
					}
				}
				current = current.getSuperclass();
			}
		}
	}

	public static void traverseAllSuperClasses(final Class<?> clazz, Function<Class<?>, Boolean> visitor) {
		if (clazz != null) {
			Class<?> usedClazz = clazz.isArray() ? clazz.getComponentType() : clazz;
			if (!usedClazz.isInterface() && usedClazz != Object.class) {
				Class<?> superClass = usedClazz.getSuperclass();
				while (superClass != null) {
					if (!visitor.apply(superClass)) {
						break;
					}
					superClass = superClass.getSuperclass();
				}
			}
		}
	}

	public static Class<?> tryAsClass(String className) {
		return tryAsClass(className, null);
	}

	public static Class<?> tryAsClass(String className, ClassLoader classLoader) {
		if (StrUtil.isBlank(className)) {
			return null;
		}
		try {
			return asClass(ObjectUtils.defaultObject(classLoader, ClassesUtils::defaultClassLoader), className, true);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}


}
