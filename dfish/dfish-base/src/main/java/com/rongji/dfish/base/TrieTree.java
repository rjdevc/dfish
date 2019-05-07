package com.rongji.dfish.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rongji.dfish.base.TrieTree.Node;

/**
 * 字典树类
 * <p>用于多关键词的字符串匹配，利用字符串的公共前缀来减少查询时间，最大限度地减少无谓的字符串比较，查询效率比哈希树高。</p>
 * <p>可参照<a href="http://baike.baidu.com/view/2759664.htm">百度百科-字典树</a>(http://baike.baidu.com/view/2759664.htm)了解其更多信息。<p>
 * <p>这里封装的字典树除了树本身，还允许节点上存储内容，以便扩展更多的应用。因为其应用很类似于java.util.Map。所以方法命名和参数，向Map靠近。</p>
 * 1.0 
 * [完成] 要支持 remove (字典树比较特殊revome完要优化树，删除不必要的节点，所以如果有批量删除，
 * [完成] 最好最后优化，可能做为不同的方法)
 * [完成] 需要支持 entryList方法以便对所有的词进行遍历。和HashSet不一样，这个entryList仅仅是所有值的体现。
 * [完成] 需要支持 复制构造函数 new TrieTree(TrieTree another,boolean reverse);
 * [完成] 支持 reverse=true时候的 searchAll
 * [完成] 可能的话，node的childen制作一个专门的CharHashMap来提高效率。
 * 
 * @author DFish Team v0.8曲戈 v1.0 林利炜
 * @param <V> 容纳的内容类型
 * @version 1.0.20160801
 * 
 */
public class TrieTree<V extends Object> {
	/**
	 * 树的根节点
	 */
	protected Node<V> root;
	/**
	 * 是否反序匹配，字符串从又到左的顺序匹配。
	 * 中文一般定语在前(左)，主要词义在后(右)，有时候从右到左匹配到的词组更能贴合自然语言
	 */
	private boolean reverse;

	/**
	 * 构造函数
	 */
	public TrieTree() {
		this(false);
	}
	/**
	 * 构造函数
	 * @param reverse 是否反序匹配，从右向左
	 */
	public TrieTree(boolean reverse) {
		this.reverse=reverse;
		root = new Node<V>();
	}
	/**
	 * 构造函数
	 * @param another 根据该树重新构建
	 * @param reverse 是否反序匹配，从右向左
	 */
	@SuppressWarnings("unchecked")
	public TrieTree(TrieTree<? extends V> another,boolean reverse) {
		this.reverse=reverse;
		root = new Node<V>();
		List<?> entryList=another.entryList();
		for(Object o:entryList){
			Entry<?> entry=(Entry<?>)o;
			put(entry.getKey(),(V) entry.getValue());
		}
	}
	/**
	 * 构造函数，复制一另一个字典树的数据，相当于clone
	 * @param another
	 */
	public TrieTree(TrieTree<? extends V> another) {
		this(another,another.reverse);
	}

	/**
	 * 该字典树是否已经包含指定的词
	 * @param key 关键词
	 * @return boolean
	 */
	public boolean containsKey(String key) {
		Node<V> current = root;
		if(reverse){
			char[] chs=key.toCharArray();
			for (int i=chs.length-1;i>=0;i-- ) {
				Node<V> child = current.get(chs[i]);
				if (child == null) {
					return false;
				} else {
					current = child;
				}
			}
		}else{
			for (char ch : key.toCharArray()) {
				Node<V> child = current.get(ch);
				if (child == null) {
					return false;
				} else {
					current = child;
				}
			}
		}
		return current.end;
	}

	/**
	 * 把某个关键词，以及其对应的内容放入到字典树，
	 * 如果这个关键词已经包含内容了，那么将返回原有的内容。
	 * @param key String 关键词
	 * @param value V 内容
	 * @return V 原有的内容
	 */
	public V put(String key, V value) {//北京市  北极
		Node<V> current = root;
		if(reverse){
			char[] chs=key.toCharArray();
			for (int i=chs.length-1;i>=0;i-- ) {
				Node<V> child = current.get(chs[i]);
				if (child != null) {
					current = child;
				} else {
					Node<V> nextNode = new Node<V>();
					current.put(chs[i],nextNode);
					current = nextNode;
				}
			}
		}else{
			for (char ch : key.toCharArray()) {
				Node<V> child = current.get(ch);
				if (child != null) {
					current = child;
				} else {
					Node<V> nextNode = new Node<V>();
					current.put(ch,nextNode);
					current = nextNode;
				}
			}
		}
		// Set isEnd to indicate end of the word
		V oldValue=current.value;
		current.value = value;
		current.end = true;
		return oldValue;
	}

	/**
	 * 取得关键词对应的内容
	 * @param key String 关键词
	 * @return V 内容
	 */
	public V get(String key) {
		Node<V> find=find(key);
		if(find==null){return null;}
		return find.value;
	}

	
	/**
	 * 删除某个节点，注意删除这个节点后，可能会留下一些空分支，
	 * 可以调用optimize方法进行优化分支。
	 * 如果不优化，将会稍微影响性能。
	 * 
	 * 如果有多次删除建议全部删除完以后，调用一次optimize方法。
	 * @param key String
	 * @return 删除掉的节点值
	 */
	public V remove(String key) {
		Node<V> find=find(key);
		if(find==null){return null;}
		V oldValue=null;
		if(find.end){
			oldValue=find.value;
			find.value=null;
			find.end=false;
		}
		return oldValue;
	}
	protected Node<V> find(String key){
		Node<V> current = root;
		if(reverse){
			char[] chs=key.toCharArray();
			for (int i=chs.length-1;i>=0;i-- ) {
				Node<V> child = current.get(chs[i]);
				if (child != null) {
					current = child;
				} else {
					return null;
				}
			}
		}else{
			for (char c : key.toCharArray()) {
				Node<V> child = current.get(c);
				if (child != null) {
					current = child;
				} else {
					return null;
				}
			}
		}
		return current;
	}
	/**
	 * 优化字典树
	 * 删除字典树内容的时候，可能会导致空分支，而影响字典树效率。
	 * 调用该方法优化字典树。
	 * 该方法本身性能不高。
	 * 如果有多次删除，建议全部删除完以后，调用一次optimize方法。
	 */
	public void optimize(){
		root.deleteEmptyBranch();
	}
	
	/**
	 * 在指定的文本中查找字典树中相关的内容。
	 * 找到的每个关键字将会匹配出来，默认匹配更长的关键字
	 * <div style="border:1px solid gray">
	 * 如果同时有北京市和北京两个关键字，默认匹配北京市，如果北京后面没有带市则匹配北京。<br/>
	 * 如果这时候同时有北京市 和 市委两个关键词，而内容为 北京市委  根据最长匹配规则 北京市匹配到了，后面的委字则独立出来，匹配不出市委这个词。<br/>
	 * 有时候为了提高中文匹配度，会使用从右向左匹配new TrieTree(true)，或者不管当前匹配到多长的关键字，都从后面一个字符开始匹配。searchAll<br/>
	 * 这类信息可以了解搜索引擎的中文切词器
	 * </div>
	 * @param text 内容
	 * @return List
	 */
	public List<SearchResult<V>> search(String text) {
		List<SearchResult<V>> searchResult = new ArrayList<SearchResult<V>>();
		char[] content = text.toCharArray();
		if(reverse){
			for (int i = content.length; i >0; i--) {
				SearchResult<V> r = root.matchReverse(content, 0, i);
				if (r!=null) {
					searchResult.add(r);
					i -= (r.end - r.begin - 1);
				}
			}
			java.util.Collections.reverse(searchResult);
		}else{
			for (int i = 0; i < content.length; i++) {
				SearchResult<V> r = root.match(content, i, content.length);
				if (r!=null) {
					searchResult.add(r);
					i += (r.end - r.begin - 1);
				}
			}
		}
		return searchResult;
	}
	/**
	 * 尝试匹配内容，这里尝试最全的匹配
	 * 比如 杭州市长春药店 匹配的结果是
	 * 杭州 杭州市 市长 长春 春药 药店 (如果这些词在词库中都有的话)
	 * 注意：不支持reverse模式
	 * @param text 内容
	 * @return List
	 */
	public List<SearchResult<V>> searchAll(String text) {
		List<SearchResult<V>> searchResult = new ArrayList<SearchResult<V>>();
		char[] content = text.toCharArray();
		if(reverse){
			for (int i = content.length; i >0; i--) {
				List<SearchResult<V>> r = root.matchAllRevers(content, 0, i);
				searchResult.addAll(r);
			}
			Collections.reverse(searchResult);
		}else{
			for (int i = 0; i < content.length; i++) {
				List<SearchResult<V>> r = root.matchAll(content, i, content.length);
				searchResult.addAll(r);
			}
		}
		return searchResult;
	}
	/**
	 * 搜索字符串的，返回结果
	 *
	 * @param <V> 内容的类型
	 */
	public static class SearchResult<V> {
		/**
		 * 默认构造函数
		 * @param begin 开始字节数
		 * @param end 结束字节数
		 * @param value 结果内容
		 */
		public SearchResult(int begin, int end,V value) {
			this.begin = begin;
			this.end = end;
			this.value=value;
		}

		private int begin;
		private int end;
//		private boolean match;
		private V value;
		
		/**
		 * 该搜索结果开始于源字符串的第几个字符
		 * @return int
		 */
		public int getBegin() {
			return begin;
		}


		/**
		 * 该搜索结果结束于源字符串的第几个字符
		 * @return int
		 */
		public int getEnd() {
			return end;
		}

//		/**
//		 * 是否匹配，一般来说只有匹配上的才会返回个调用方，所以一般为true<br/>
//		 * false 一般只出现在运算过程当中
//		 * @return boolean
//		 */
//		public boolean isMatch() {
//			return match;
//		}


		/**
		 * 字典树中映射的内容
		 * @return V
		 */
		public V getValue() {
			return value;
		}



	}
	/**
	 * Hash 节点
	 * 因为key是char 所以省略了hash字段，节省了内存和运算。
	 *
	 * @param <V> 值类型
	 */
	private static class HashNode<V>{
		public HashNode(char key,Node<V> value){
			this.key=key;
			this.value=value;
		}
		private char key;
		private HashNode<V> next;
		private Node<V> value;
	}
	/**
	 * 字典树的节点
	 * 
	 * TrieNode 字典树节点，主要关注当前的词是否结束了所以有end属性，children和value
	 *
	 * @param <V> 内容类型
	 */
	public static class Node<V> {
		protected boolean end; //是否一个词结束了。
		private HashNode<V>[] children;//注意这是Hashtable格式。并不是所有的空间都有值，并且有些值有重复的，可能是以链表的方式存在的
		private int threshold;//hash表的参数，我们使用默认的参数，如果children的空间是16这个就是它的3/4=12如果hashtable中放置超过12(含列表内的数量)则会触发增加
		protected int size;
		protected V value;
		
		
		private static final int DEFAULT_INITIAL_CAPACITY=16;
		private static final char CHAR_BLANK = '\u3000';//全角空格
		private static final char CHAR_I = '\u2502';//制表符 │
		private static final char CHAR_T = '\u251C';//制表符├
		private static final char CHAR_L = '\u2514';//制表符└

		public Node() {}

		/**
		 * 是否一个词结束了。
		 * @return boolean
		 */
		public boolean isEnd() {
			return end;
		}


		/**
		 * 是否一个词结束了。
		 * @param end boolean
		 */
		public void setEnd(boolean end) {
			this.end = end;
		}
		/**
		 * 获取值
		 * @return V
		 */
		public V getValue() {
			return value;
		}

		/**
		 * 设置值
		 * @param value V
		 */
		public void setValue(V value) {
			this.value = value;
		}
		/**
		 * 这个节点下有几个内容(不含级联)
		 * @return int
		 */
		public int getSize() {
			return size;
		}
		/**
		 * 这个节点下有几个内容(不含级联)
		 * @param size int
		 */
		public void setSize(int  size) {
			this.size = size;
		}


		/**
		 * 查找子节点，如果指定的字符不存在子节点。那么返回空。
		 * @param key 下一个字符
		 * @return Node
		 */
		public Node<V> get(char key) {
			HashNode<V>[] tab; 
			HashNode<V> first, e; 
			int n; 
	        if ((tab = children) != null && (n = tab.length) > 0 &&
	            (first = tab[(n - 1) & key]) != null) {
	            if (first.key == key ){
	                return first.value;
	            }
	            if ((e = first.next) != null) {
	                do {
	                    if (e.key == key ){
	                        return e.value;
	                    }
	                } while ((e = e.next) != null);
	            }
	        }
	        return null;
		}

		/**
		 * 设置子节点，如果这个关键字下已经存在子节点，将会替换，并返回原来的节点的内容。
		 * @param key 关键字
		 * @param child 子节点
		 * @return 原来的子节点
		 */
		public Node<V> put(char key,Node<V> child) {
			HashNode<V>[] tab;
			HashNode<V> p; 
			int n, i;
	        if ((tab = children) == null || (n = tab.length) == 0){
	            n = (tab = resize()).length;
	        }
	        if ((p = tab[i = (n - 1) & key]) == null){
	            tab[i] = new HashNode<V>(key,child);
	        }else {
	        	HashNode<V> e; 
	            if (p.key == key )
	                e = p;
	            else {
	                while (true) {
	                    if ((e = p.next) == null) {
	                        p.next = new HashNode<V>(key,child);
	                        break;
	                    }
	                    if (e.key == key ){
	                        break;
	                    }
	                    p = e;
	                }
	            }
	            if (e != null) { // existing mapping for key
	                Node<V> oldValue = e.value;
	                e.value = child;
	                return oldValue;
	            }
	        }
	        if (++size > threshold){
	            resize();
	        }
	        return null;
		}
		/**
		 * 将当前用于容纳哈希数组的容量翻倍。
		 * 或新建一个(如果还没有的话)
		 * @return 新建或翻倍过的数组
		 */
		private HashNode<V>[] resize() {
			HashNode<V>[] oldTab = children;
	        int oldCap = (oldTab == null) ? 0 : oldTab.length;
	        int oldThr = threshold;
	        int newCap, newThr = 0;
	        if (oldCap > 0) {
	            newCap = oldCap << 1;
	             newThr = oldThr << 1; // double threshold
	        } else {               // zero initial threshold signifies using defaults
	            newCap = DEFAULT_INITIAL_CAPACITY;
	            newThr = DEFAULT_INITIAL_CAPACITY * 3 / 4;
	        }
	        threshold = newThr;
	        @SuppressWarnings({"unchecked"})
	        HashNode<V>[] newTab = (HashNode<V>[])new HashNode[newCap];
	        children = newTab;
	        if (oldTab != null) {
	            for (int j = 0; j < oldCap; ++j) {
	            	HashNode<V> e;
	                if ((e = oldTab[j]) != null) {
	                    oldTab[j] = null;
	                    if (e.next == null){
	                        newTab[e.key & (newCap - 1)] = e;
	                    }else { // preserve order
	                    	HashNode<V> loHead = null, loTail = null;
	                    	HashNode<V> hiHead = null, hiTail = null;
	                    	HashNode<V> next;
	                        do {
	                            next = e.next;
	                            if ((e.key & oldCap) == 0) {
	                                if (loTail == null)
	                                    loHead = e;
	                                else
	                                    loTail.next = e;
	                                loTail = e;
	                            } else {
	                                if (hiTail == null)
	                                    hiHead = e;
	                                else
	                                    hiTail.next = e;
	                                hiTail = e;
	                            }
	                        } while ((e = next) != null);
	                        if (loTail != null) {
	                            loTail.next = null;
	                            newTab[j] = loHead;
	                        }
	                        if (hiTail != null) {
	                            hiTail.next = null;
	                            newTab[j + oldCap] = hiHead;
	                        }
	                    }
	                }
	            }
	        }
	        return newTab;
		}
		/**
		 * 尝试匹配内容，并且这里匹配的是最长的字符串。
		 * 比如有北京市和北京。如果当前节点是【京】它并不会马上得到结果，而是尝试匹配更长的字符。
		 * 匹配的上，则返回更长的关键字 北京市；匹配不上则返回当前结果 北京。
		 * @param content 匹配的内容
		 * @param begin 开始匹配的位置-针对于 匹配的内容
		 * @param end 结束匹配的位置-针对于匹配的内容，一般不限制结束匹配的位置
		 * @return SearchResult 仅返回最长的一个结果。没有返回所有结果。
		 */
		public SearchResult<V> match(char[] content, int begin, int end) {
			char c = content[begin];
			
			if (children == null) {
				return null;
			}
			Node<V> p = get(c);
			SearchResult<V> result = null;
			if (p == null) {
				return null;
			} else {
				if (p.end) {
					result=new SearchResult<V> (begin,begin+1,p.value);
				}
				if (p.children != null) {
					if (begin + 1 < end) {
						SearchResult<V> subMatch = p.match(content, begin + 1, end);
						if (subMatch!=null) {
							return new SearchResult<V> (begin,subMatch.end,subMatch.value);
						}
					}
				}
			}
			return result;
		}
		/**
		 * 尝试匹配内容，并且这里反向匹配(从右向左)最长的字符串。
		 * 比如有长沙县和沙县。如果当前节点是【沙】它并不会马上得到结果，而是尝试匹配更长的字符。
		 * 匹配的上，则返回更长的关键字 长沙县；匹配不上则返回当前结果 沙县。
		 * @param content 匹配的内容
		 * @param begin 开始(数字比较小的)匹配的位置-针对于 匹配的内容，一般不限制结束匹配的位置，一般为0；
		 * @param end 结束(数字比较大的)匹配的位置-针对于匹配的内容
		 * @return SearchResult 仅返回最长的一个结果。没有返回所有结果。
		 */
		public SearchResult<V> matchReverse(char[] content, int begin, int end ) {
			char c = content[end-1];
			if (children == null) {
				return null;
			}
			SearchResult<V> result = null;
			Node<V> p = get(c);
			if (p == null) {
				return null;
			} else {
				if (p.end) {
					result=new SearchResult<V>(end-1,end,p.value);
				}
				if (p.children != null) {
					if (begin + 1 < end) {
						SearchResult<V> subMatch = p.matchReverse(content, begin , end-1);
						if (subMatch!=null) {
							return new SearchResult<V>(subMatch.begin,end,subMatch.value) ;
						}
					}
				}
			}
			return result;
		}
		/**
		 * 尝试匹配内容，这里尝试最全的匹配
		 * 比如 杭州市长春药店 匹配的结果是
		 * 杭州 杭州市 市长 长春 春药 药店 (如果这些词在词库中都有的话)
		 * @param content 匹配的内容
		 * @param begin 开始匹配的位置-针对于 匹配的内容，
		 * @param end 结束匹配的位置-针对于匹配的内容，一般不限制结束匹配的位置，一般为content.length；
		 * @return SearchResult 仅返回最长的一个结果。没有返回所有结果。
		 */
		public List<SearchResult<V>> matchAll(char[] content, int begin, int end ) {
			List<SearchResult<V>>result =new ArrayList<SearchResult<V>>();
			doMatchAll(content, begin,begin, end, result);
			return result;
		}
		private void doMatchAll(char[] content, int wordbegin,int matchbegin, int end ,List<SearchResult<V>>result) {
			char c = content[matchbegin];
			if (children == null) {
				return;
			}
			Node<V> p = get(c);
			if (p == null) {
				return;
			} else {
				if (p.end) {
					result.add(new SearchResult<V>(wordbegin, matchbegin + 1,p.value));
				}
				if (p.children != null) {
					if (matchbegin + 1 < end) {
						p.doMatchAll(content, wordbegin,matchbegin + 1, end,result);
					}
				}
			}
		}
		/**
		 * 尝试匹配内容，这里尝试最全的匹配
		 * 比如 杭州市长春药店 匹配的结果是
		 * 杭州 杭州市 市长 长春 春药 药店 (如果这些词在词库中都有的话)
		 * @param content 匹配的内容
		 * @param begin 开始匹配的位置-针对于 匹配的内容，
		 * @param end 结束匹配的位置-针对于匹配的内容，一般不限制结束匹配的位置，一般为content.length；
		 * @return SearchResult 仅返回最长的一个结果。没有返回所有结果。
		 */
		public List<SearchResult<V>> matchAllRevers(char[] content, int begin, int end ) {
			List<SearchResult<V>>result =new ArrayList<SearchResult<V>>();
			doMatchAllReverse(content, begin,end, end, result);
//			Collections.reverse(result);
			return result;
		}
		private void doMatchAllReverse(char[] content, int begin,int matchend, int wordend ,List<SearchResult<V>>result) {
			char c = content[matchend-1];
			if (children == null) {
				return;
			}
			Node<V> p = get(c);
			if (p == null) {
				return;
			} else {
				if (p.end) {
					result.add(new SearchResult<V>(matchend-1, wordend,p.value));
				}
				if (p.children != null) {
					if (begin + 1 < matchend) {
						p.doMatchAllReverse(content, begin,matchend -1, wordend,result);
					}
				}
			}
		}

		/**
		 * 把字典树转化成缩进的格式输出，以便更容易读懂
		 * 
		 * @param prefix 前缀，做缩进的字符
		 * @param key 节点关键字
		 * @param sb 字符串累积到该StringBuilder中，一并输出
		 * @param standalone 尝试把几个关键字合并到一起输出，以便输出内容不会太长。所以这里设定是否独立节点。如果非独立节点就进行合并。
		 */
		public void toString(String prefix, Character key, StringBuilder sb,
				boolean standalone) {
			if (size == 1 && !end) {
				// 如果没有结果，并且只有一个子节点。可以考虑叠加到一起显示。
				if (standalone) {
					sb.append(prefix);
				}
				sb.append(key);
				// 取得唯一的值
				for (HashNode<V> node : children) {
					if (node != null) {
						node.value.toString(prefix, node.key, sb, false);
						break;
					}
				}
				return;
			} else {
				if (standalone) {
					sb.append(prefix);
				}
				sb.append(key);
				if (end) {
					sb.append('=');
					sb.append(value);
				}
				sb.append("\r\n");
			}
			if (size == 0) {
				return;
			}
			String newPrefix = "";
			if (prefix.length() > 1) {
				newPrefix = prefix.substring(0, prefix.length() - 1);
			}
			if (prefix.length() > 0) {
				char lastChar = prefix.charAt(prefix.length() - 1);
				if (lastChar == CHAR_L) {
					newPrefix += CHAR_BLANK;
				} else if (lastChar == CHAR_T) {
					newPrefix += CHAR_I;
				}
			}
			int i = 0;
			for (HashNode<V> first : children) {
				HashNode<V> node = first;
				while (node != null) {
					if (++i < size) {
						node.value.toString(newPrefix + CHAR_T, node.key, sb, true);
					} else {
						node.value.toString(newPrefix + CHAR_L, node.key, sb, true);
					}
					node = node.next;
				}
			}

		}
		/**
		 * 深度优先前序遍历找到所有节点
		 * @param prefix 前缀
		 * @param result 用于存放结果集的列表
		 */
		public void findEntryList (String prefix,List<Entry<V>> result){
			if(children!=null){
				for(HashNode<V> first:children){
					HashNode<V> node=first;
					while(node!=null){
						String nextPrefix=prefix+node.key;
						if(node.value.end){
							result.add(new Entry<V>( nextPrefix,node.value.value));
						}
						node.value.findEntryList(nextPrefix,result);
						node=node.next;
					}
				}
			}
		}
		/**
		 * 优化字典树删除所有空分支
		 * @return 返回子分支有多少个。如果子分支为0.有可能该分支也要被清理。
		 */
		public int deleteEmptyBranch() {
			int subWords=end?1:0;
			if(children!=null){
				for(int i=0;i<children.length;i++){
					HashNode<V> first=children[i];
					HashNode<V> parent=null;
					HashNode<V> node=first;
					while(node!=null){
						int count=node.value.deleteEmptyBranch();
						if(count==0){
							//删除当前节点，如果当前节点是hashtable的直接格子。那么直接删除，否则从链表上删除。
							if(parent==null){
								children[i]=null;
							}else{
								parent.next=node.next;
							}
							size--;
						}
						subWords+=count;
						parent=node;
						node=node.next;
					}
				}
			}
			return subWords;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if(children!=null){
				for(HashNode<V> first:children){
					HashNode<V> node=first;
					while(node!=null){
						node.value.toString("", node.key,  sb, true);
						node=node.next;
					}
				}
			}
			return sb.toString();
		}
	}

	public String toString() {
		return root.toString();
	}
	/**
	 * 把所有的节点数据读取出来，以便复制，备份等工作。
	 * @return List
	 */
	public List<Entry<V>> entryList(){
		List<Entry<V>> result=new ArrayList<Entry<V>>();
		//把所有的节点读取出来，以便复制，备份等工作。
		root.findEntryList("",result);
		return result;
	}
	/**
	 * Entry 一个数据结构用于返回entryList的结果。该结果可以
	 * 
	 * @param <V> 值的类型
	 */
	public static class Entry<V>{
		private String key;
		private V value;
		/**
		 * 默认构造函数
		 */
		public Entry(){}
		/**
		 * 构造函数
		 * @param key 关键字
		 * @param value 对应的值。
		 */
		public Entry(String key,V value){
			this.key=key;
			this.value=value;
		}
		/**
		 * 关键字
		 * @return String
		 */
		public String getKey() {
			return key;
		}
		/**
		 * 对应的值
		 * @return String
		 */
		public V getValue() {
			return value;
		}
	
		public String toString(){
			return key+"="+value;
		}
	}
	/**
	 * 获取根节点
	 * @return root
	 */
	public Node<V> getRoot() {
		return root;
	}

}
