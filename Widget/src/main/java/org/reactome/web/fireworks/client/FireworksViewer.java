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
@SuppressWarnings("UnusedDeclaration")
public interface FireworksViewer extends IsWidget, HasHandlers, RequiresResize {

    public HandlerRegistration addAnalysisResetHandler(AnalysisResetHandler handler);

    public HandlerRegistration addCanvasNotSupportedHandler(CanvasNotSupportedHandler handler);

    public HandlerRegistration addExpressionColumnChangedHandler(ExpressionColumnChangedHandler handler);

    public HandlerRegistration addNodeHoverHandler(NodeHoverHandler handler);

    public HandlerRegistration addNodeHoverResetHandler(NodeHoverResetHandler handler);

    public HandlerRegistration addNodeOpenedHandler(NodeOpenedHandler handler);

    public HandlerRegistration addNodeSelectedHandler(NodeSelectedHandler handler);

    public HandlerRegistration addNodeSelectedResetHandler(NodeSelectedResetHandler handler);

//    public HandlerRegistration addFireworksZoomHandler(FireworksZoomEventHandler handler);

    public Node getSelected();

    public void highlightNodeByStableIdentifier(String stableIdentifier);

    public void highlightNodeByDbIdentifier(Long dbIdentifier);

    public void resetAnalysis();

    public void resetHighlight();

    public void resetSelection();

    public void selectNodeByStableIdentifier(String stableIdentifier);

    public void selectNodeByDbIdentifier(Long dbIdentifier);

    public void setAnalysisToken(String token, String resource);

}
