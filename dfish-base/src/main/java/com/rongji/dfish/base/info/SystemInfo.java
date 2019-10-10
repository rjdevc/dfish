package com.rongji.dfish.base.info;

/**
 * 系统信息类
 * @author DFish Team
 *
 */
public final class SystemInfo {

  private String vmName;
  private String vmVendor;
  private String vmVersion;
  private String runtimeName;
  private String runtimeVersion;
  private String operationSystem;
  private String cpu;
  private String fileEncoding;
  private String javaVersion;

  /**
   * 构造函数
   */
  public SystemInfo() {
    vmName = getProperty("java.sp.name");
    vmVendor = getProperty("java.sp.vendor");
    vmVersion = getProperty("java.sp.version");
    runtimeName = getProperty("java.runtime.name");
    runtimeVersion = getProperty("java.runtime.version");
    operationSystem = getProperty("os.name") + " " + getProperty("os.version") +
        "," + getProperty("os.arch");
    cpu = getProperty("sun.cpu.isalist");
    fileEncoding = getProperty("file.encoding");
    javaVersion = getProperty("java.sp.version");
  }

  public static String getProperty(String key) {
    String retValue = null;
    try {
      retValue = System.getProperty(key, "");
    }
    catch (Exception ex) {
      retValue = "--------";
    }
    return retValue;
  }

  public String getCpu() {
    return cpu;
  }

  /**
   * 取得操作系统名
   */
  public String getOperationSystem() {
    return operationSystem;
  }

  public String getRuntimeName() {
    return runtimeName;
  }

  public String getRuntimeVersion() {
    return runtimeVersion;
  }

  public String getVmName() {
    return vmName;
  }

  public String getVmVendor() {
    return vmVendor;
  }

  public String getVmVersion() {
    return vmVersion;
  }

  /**
   * 取得文件系统字符集
   * @return
   */
  public String getFileEncoding() {
    return fileEncoding;
  }

  /**
   * 取得JAVA版本号
   * @return
   */
  public String getJavaVersion() {
    return javaVersion;
  }
  /**
   * 取得空闲内存大小
   * @return
   */
  public long getFreeMemory() {
    Runtime runtime = Runtime.getRuntime();
    return runtime.freeMemory();
  }
  /**
   * 取得总的内存大小
   * @return
   */
  public long getTotalMemory() {
    Runtime runtime = Runtime.getRuntime();
    return runtime.totalMemory();
  }

}
