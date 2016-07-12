package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.GraphEntryHoveredHandler;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GraphEntryHoveredEvent extends GwtEvent<GraphEntryHoveredHandler> {
    public static Type<GraphEntryHoveredHandler> TYPE = new Type<>();

    private GraphEntry hoveredEntry;

    public GraphEntryHoveredEvent(GraphEntry hoveredEntry) {
        this.hoveredEntry = hoveredEntry;
    }

    @Override
    public Type<GraphEntryHoveredHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GraphEntryHoveredHandler handler) {
        handler.onGraphEntryHovered(this);
    }

    public GraphEntry getHoveredEntry() {
        return hoveredEntry;
    }

    @Override
    public String toString() {
        return "GraphEntryHoveredEvent{" +
                "hovered=" + (hoveredEntry!= null ? hoveredEntry.getStId():null) +
                '}';
    }
}