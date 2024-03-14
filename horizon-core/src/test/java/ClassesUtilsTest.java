import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redcarp.horizon.core.util.ClassesUtils;
import org.redcarp.horizon.core.util.StreamUtils;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author redcarp
 * @date 2024/3/14
 */
public class ClassesUtilsTest {

	@Test
	public void asClass() {
		Class<?> aClass = ClassesUtils.asClass("java.lang.String");
		Assertions.assertEquals(aClass.getName(), "java.lang.String");
	}

	@Test
	public void tryAsClass() {
		Class<?> tryAsClass = ClassesUtils.tryAsClass("java.lang.Horizon");
		Assertions.assertNull(tryAsClass);
	}

	@Test
	public void getAllInterfaces() {
		Set<Class<?>> interfaces = ClassesUtils.getAllInterfaces(ArrayList.class);
		interfaces.forEach(inf -> System.out.println("aClass.getName() = " + inf.getName()));
	}

	@Test
	public void getPackageName() {
		String packageName = ClassesUtils.getPackageName("java.util.ArrayList");
		Assertions.assertEquals(packageName, "java.util");
	}

	@Test
	public void getAllSuperClasses() {
		Set<Class<?>> allSuperClasses = ClassesUtils.getAllSuperClasses(ArrayList.class);
		StreamUtils.streamOf(allSuperClasses).forEach(aClass -> System.out.println("aClass.getName()=" + aClass.getName()));
	}

}
