package com.rongji.dfish.framework.util;

import org.apache.commons.logging.Log;

@Deprecated
public class WrappedLog implements Log{
		Log core;
		
		public WrappedLog(Log log) {
			this.core=log;
		}

		@Override
		public boolean isDebugEnabled() {
			return core.isDebugEnabled();
		}

		@Override
		public boolean isErrorEnabled() {
			return core.isErrorEnabled();
		}

		@Override
		public boolean isFatalEnabled() {
			return core.isFatalEnabled();
		}

		@Override
		public boolean isInfoEnabled() {
			return core.isInfoEnabled();
		}

		@Override
		public boolean isTraceEnabled() {
			return core.isTraceEnabled();
		}

		@Override
		public boolean isWarnEnabled() {
			return core.isWarnEnabled();
		}

		@Override
		public void trace(Object o) {
			core.trace(o);
		}

		@Override
		public void trace(Object o, Throwable t) {
			core.trace(o,t);
			if(core.isTraceEnabled()){
				// TODO
//				ExceptionManager.getInstance().log(t);
			}
		}

		@Override
		public void debug(Object o) {
			core.debug(o);
		}

		@Override
		public void debug(Object o, Throwable t) {
			core.debug(o,t);
			if(core.isDebugEnabled()){
				// TODO
//				ExceptionManager.getInstance().log(t);
			}
		}

		@Override
		public void info(Object o) {
			core.info(o);
		}

		@Override
		public void info(Object o, Throwable t) {
			core.info(o,t);
			if(core.isInfoEnabled()){
				// TODO
//				ExceptionManager.getInstance().log(t);
			}
		}

		@Override
		public void warn(Object o) {
			core.warn(o);
		}

		@Override
		public void warn(Object o, Throwable t) {
			core.warn(o,t);
			if(core.isWarnEnabled()){
				// TODO
//				ExceptionManager.getInstance().log(t);
			}
		}

		@Override
		public void error(Object o) {
			core.error(o);
		}

		@Override
		public void error(Object o, Throwable t) {
			core.error(o,t);
			if(core.isErrorEnabled()){
				// TODO
//				ExceptionManager.getInstance().log(t);
			}
		}

		@Override
		public void fatal(Object o) {
			core.fatal(o);
		}

		@Override
		public void fatal(Object o, Throwable t) {
			core.fatal(o,t);
			if(core.isFatalEnabled()){
				// TODO
//				ExceptionManager.getInstance().log(t);
			}
		}
		
}
