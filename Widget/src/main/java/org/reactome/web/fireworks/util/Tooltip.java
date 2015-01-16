package org.reactome.web.fireworks.util;

import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Tooltip extends PopupPanel {
    private static Tooltip tooltip;

    private boolean preventShowing = false;

    private Tooltip(){
        super(true);
    }

    public static Tooltip getTooltip(){
        if(tooltip==null){
            tooltip = new Tooltip();
        }
        return tooltip;
    }

    public void show(CanvasElement sender, int offsetX, int offsetY, double nodeSize, Widget widget) {
        if(preventShowing) return; //If the node is not visible, preventShowing has to be set to false previously

        //setStyleName clears previously set bubble types
        FireworksStyleFactory.FireworksStyle style = FireworksStyleFactory.getAnalysisStyle();
        style.ensureInjected();
        setStyleName(style.bubble());
        sender.getParentElement().getStyle().setOverflow(Style.Overflow.VISIBLE);

        clear();
        add(widget);

        setVisible(false);
        super.show();
        int left; int top;
        int size = (int) Math.ceil(nodeSize) + 4;
        if(offsetX < sender.getWidth()/2) {
            left = sender.getAbsoluteLeft() + offsetX - 12;
            if((offsetY - size) < 50){
                top = sender.getAbsoluteTop() + offsetY + size;
                addStyleName(FireworksStyleFactory.getAnalysisStyle().bubbleTopLeft());
            }else{
                top = sender.getAbsoluteTop() + offsetY - getOffsetHeight() - size;
                addStyleName(FireworksStyleFactory.getAnalysisStyle().bubbleBottomLeft());
            }
        }else{
            left = sender.getAbsoluteLeft() + offsetX - getOffsetWidth() + 12;
            if((offsetY - size)< 50){
                top = sender.getAbsoluteTop() + offsetY + size;
                addStyleName(FireworksStyleFactory.getAnalysisStyle().bubbleTopRight());
            }else{
                top = sender.getAbsoluteTop() + offsetY - getOffsetHeight() - size;
                addStyleName(FireworksStyleFactory.getAnalysisStyle().bubbleBottomRight());
            }
        }
        setPopupPosition(left, top);
        setVisible(true);
    }

    public void setPreventShowing(boolean preventShowing) {
        this.preventShowing = preventShowing;
        if(preventShowing && isVisible()) this.hide();
    }
}