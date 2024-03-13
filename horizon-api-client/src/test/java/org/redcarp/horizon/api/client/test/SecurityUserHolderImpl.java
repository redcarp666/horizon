package org.redcarp.horizon.api.client.test;

import org.redcarp.horizon.core.security.SecurityUserHolder;
import org.springframework.stereotype.Component;

/**
 * @author redcarp
 * @date 2024/3/13
 */
@Component
public class SecurityUserHolderImpl implements SecurityUserHolder {
	@Override
	public String getCurrentUserId() {
		return null;
	}

	@Override
	public String getCurrentUserName() {
		return null;
	}

	@Override
	public String getCurrentToken() {
		return "eyJhbGciOiJSUzI1NiJ9.eyJuYW1lIjoiSmVycnkiLCJzdWIiOiIxNzY3MzY5ODQyNzA1NDk0MDE3IiwiZXhwIjoxNzEwMzI0ODM1LCJpYXQiOjE3MTAzMjEyMzUsInJvbGVzIjpbXX0.i_SEBwsePqsiQYZxUh78luyoQRTvO-ChLfkw7gz6MKb7XaLy68my6IGhJWbF5hPkvKWdbeWt20GONSwuZtPdAwndb4o4mgm3h5_TWNbAWjBM_Lq3XAcu_nD4No79nuizf6ARRq4hMVndVc6oOzE7chCYg6-HDuRzJ7pw37KAmLLP2adom_bagjm91nBBmAtqqhsK0jbzD1-KKsUVxrFk5yo0aiJi5HD2Z9Klqse_5EWWSCUemCTXYQf-lVWvF8-5zELPToGfv_GUwobO5yVGfVewDnqO1QcTD44nsbN09HeBYL3ATx17QdGMC-P-sIdo3cebgU832pBOeYz3ydi8og";
	}
}
