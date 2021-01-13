package top.zgod.sqlupdatecheck.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author ZGOD
 */
public class SqlUpdateCheckConfig {

    @Value("${sql-update-check.scan.enabled:true}")
    private Boolean enabledScan;

    public Boolean getEnabledScan() {
        return enabledScan;
    }
}
