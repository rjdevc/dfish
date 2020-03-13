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

  /**
   * 取得CPU信息
   * @return String
   */
  public String getCpu() {
    return cpu;
  }

  /**
   * 取得操作系统名
   *  @return String
   */
  public String getOperationSystem() {
    return operationSystem;
  }

  /**
   * 取得运行环境名
   * @return String
   */
  public String getRuntimeName() {
    return runtimeName;
  }

  /**
   * 取得运行环境版本
   * @return String
   */
  public String getRuntimeVersion() {
    return runtimeVersion;
  }

  /**
   * 取得虚拟机名字
   * @return String
   */
  public String getVmName() {
    return vmName;
  }

  /**
   * 取得虚拟机产商
   * @return
   */
  public String getVmVendor() {
    return vmVendor;
  }

  /**
   * 取得虚拟机版本
   * @return
   */
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
