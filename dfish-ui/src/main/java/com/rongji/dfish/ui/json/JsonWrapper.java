package com.rongji.dfish.ui.json;

/**
 * 基于原型构建的封装类接口
 * @author DFish Team
 * @param <P> 原型
 */
public interface JsonWrapper<P> {
	/**
	 * <p>获取封装类的原型</p>
	 * <p>获取原型比较典型的三种方案。一种是所有信息改变，都针对封装类，原型仅仅是为了展现，或构建JSON。
	 * 这种情况下，一般不要getPrototype以后对它的属性值重新设定。设置了也很快会被重新覆盖。比如Tr Td等</p>
	 * <p>另一种是，封装类仅仅是个壳，封装类上的所有方法，其实都是把值记录在原型类上的。这时候，原则上可以随意使用原型类或封装类的方法。</p>
	 * <p>而最后一种情况则复杂的多。一开始使用封装类的方法快捷构建封装类。
	 * 而后发现一些小细节用封装类不好实现，则取得getPrototype后。在Prototype上进行更改。一旦有所更改，原则上将会锁定封装类。
	 * 这以后，不再允许封装类上任何写操作，否则会报异常。同时，当前的Prototype实例也已经固定。
	 * 我们可以对这个Prototype进行一些变更。这个变更在下次getPrototype被调用的时候，仍旧保留。</p>
	 * 
	 * @return P
	 */
	P getPrototype();	
}
