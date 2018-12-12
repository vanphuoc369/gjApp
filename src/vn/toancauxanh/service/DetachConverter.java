package vn.toancauxanh.service;

import java.io.Serializable;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.lang.Classes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

public final class DetachConverter implements Converter<Object, Object, Component>, Serializable {

	private static final long serialVersionUID = 1463169907348730644L;

	@Override
	public Object coerceToUi(Object val, Component component, BindContext ctx) {
		Boolean b = (Boolean) Classes.coerce(Boolean.class, val);
		if (b != null && b.booleanValue()) {
			Event evt = new Event("onPostDetach", component);
			component.addEventListener(evt.getName(), _listener);
			Events.postEvent(evt);
		}
		return IGNORED_VALUE;
	}

	static private PostDetachListener _listener = new PostDetachListener();

	static class PostDetachListener implements EventListener<Event>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public void onEvent(Event event) {
			Component comp = event.getTarget();
			comp.removeEventListener(event.getName(), this);
			comp.detach();
		}
	}

	@Override
	public Object coerceToBean(Object val, Component component, BindContext ctx) {
		return val;
	}

}
