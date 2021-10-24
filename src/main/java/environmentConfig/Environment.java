package environmentConfig;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Key;
import org.aeonbits.owner.Config.Sources;

@Sources({ "classpath:${envOwner}.properties" })
public interface Environment extends Config {
	
	String osName();
	
	@Key("app.url")
	String appURL();
	
	@Key("app.username")
	String appUsername();

	@Key("app.password")
	String appPassword();
}
