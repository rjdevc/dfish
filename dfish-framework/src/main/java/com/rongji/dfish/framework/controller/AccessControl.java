package com.rongji.dfish.framework.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限判断注解。通常是写在Controller类前或，具体访问的方法前。
 * 一般来说如果方法前有注解将会覆盖类前的注解。
 * 该注解表示这个方法需要当前登录用户，需要有关键字(keys)中规定的至少一个权限才能访问。
 * 关键字，根据不同项目可能会是菜单流水号，菜单编号。或是角色代号等。
 * @author DFish Team
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(value={ElementType.METHOD,ElementType.TYPE})  
public @interface AccessControl {  
	/**
	 * 权限过滤的关键字，根据不同项目可能会是菜单流水号，菜单编号。或是角色代号等。
	 * 允许有多个。如果多个的情况下，任何一个key吻合，将通过判断。
	 * @return
	 */
	String[] keys();
}
