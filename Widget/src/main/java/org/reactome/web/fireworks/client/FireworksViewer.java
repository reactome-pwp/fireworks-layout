package org.reactome.web.fireworks.client;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import org.reactome.web.fireworks.handlers.*;
import org.reactome.web.fireworks.model.Node;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface FireworksViewer extends IsWidget, HasHandlers, RequiresResize {

    public HandlerRegistration addAnalysisResetEventHandler(AnalysisResetEventHandler handler);

    public HandlerRegistration addCanvasNotSupportedEventHandler(CanvasNotSupportedEventHandler handler);

    public HandlerRegistration addNodeHoverEventHandler(NodeHoverEventHandler handler);

    public HandlerRegistration addNodeHoverResetEventHandler(NodeHoverResetEventHandler handler);

    public HandlerRegistration addNodeSelectedEventHandler(NodeSelectedEventHandler handler);

    public HandlerRegistration addNodeSelectedResetEventHandler(NodeSelectedResetEventHandler handler);

//    public HandlerRegistration addFireworksZoomEventHandler(FireworksZoomEventHandler handler);

    public Node getSelected();

    public void highlightNodeByStableIdentifier(String stableIdentifier);

    public void highlightNodeByDbIdentifier(Long dbIdentifier);

    public void resetHighlight();

    public void resetSelection();

    public void selectNodeByStableIdentifier(String stableIdentifier);

    public void selectNodeByDbIdentifier(Long dbIdentifier);

    public void setAnalysisToken(String token, String resource);

    public void resetAnalysis();
}
