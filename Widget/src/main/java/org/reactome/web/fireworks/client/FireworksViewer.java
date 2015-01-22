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

    public HandlerRegistration addAnalysisResetEventHandler(AnalysisResetHandler handler);

    public HandlerRegistration addCanvasNotSupportedEventHandler(CanvasNotSupportedHandler handler);

    public HandlerRegistration addNodeHoverEventHandler(NodeHoverHandler handler);

    public HandlerRegistration addNodeHoverResetEventHandler(NodeHoverResetHandler handler);

    public HandlerRegistration addNodeSelectedEventHandler(NodeSelectedHandler handler);

    public HandlerRegistration addNodeSelectedResetEventHandler(NodeSelectedResetHandler handler);

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
