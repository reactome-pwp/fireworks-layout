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

    HandlerRegistration addAnalysisResetHandler(AnalysisResetHandler handler);

    HandlerRegistration addCanvasNotSupportedHandler(CanvasNotSupportedHandler handler);

    HandlerRegistration addFireworksLoaded(FireworksLoadedHandler handler);

    HandlerRegistration addExpressionColumnChangedHandler(ExpressionColumnChangedHandler handler);

    HandlerRegistration addNodeHoverHandler(NodeHoverHandler handler);

    HandlerRegistration addNodeHoverResetHandler(NodeHoverResetHandler handler);

    HandlerRegistration addNodeFlaggedResetHandler(NodeFlaggedResetHandler handler);

    HandlerRegistration addNodeOpenedHandler(NodeOpenedHandler handler);

    HandlerRegistration addNodeSelectedHandler(NodeSelectedHandler handler);

    HandlerRegistration addNodeSelectedResetHandler(NodeSelectedResetHandler handler);

    HandlerRegistration addProfileChangedHandler(ProfileChangedHandler handler);

    Node getSelected();

    void flagItems(String identifier);

    void flagNodes(String term, String... stIds);

    void highlightNode(String stableIdentifier);

    void highlightNode(Long dbIdentifier);

    void openPathway(String stableIdentifier);

    void openPathway(Long dbIdentifier);

    void resetAnalysis();

    void resetFlaggedItems();

    void resetHighlight();

    void resetSelection();

    void selectNode(String stableIdentifier);

    void selectNode(Long dbIdentifier);

    void setAnalysisToken(String token, String resource);

    void showAll();

    void setVisible(boolean visible);
}
