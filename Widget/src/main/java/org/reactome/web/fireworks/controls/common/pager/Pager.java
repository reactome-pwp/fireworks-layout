package org.reactome.web.fireworks.controls.common.pager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.fireworks.controls.common.IconButton;
import org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Pager extends FlowPanel {
    private static String MSG = "Page: ";

    private SolrSearchResult searchResult;
    private int currentRow;
    private int totalPages;

    private IconButton startBtn;
    private IconButton previousBtn;
    private IconButton nextBtn;
    private IconButton endBtn;
    private Label title;

    private final NumberFormat nf;

    public Pager() {
        setVisible(false);
        nf = NumberFormat.getDecimalFormat();
        init();
    }

    public HandlerRegistration addPageChangedHandler(PageChangedHandler handler){
        return addHandler(handler, PageChangedEvent.TYPE);
    }

    public void setResults(SolrSearchResult searchResult) {
        this.searchResult = searchResult;
        this.totalPages  = (int) Math.ceil(searchResult.getFound()/(double)searchResult.getRows());
        currentRow = searchResult.getStartRow();

        setVisible(totalPages>1);
        updateText();
    }

    private void init() {
        setStyleName(RESOURCES.getCSS().mainPanel());

        startBtn = new IconButton("", RESOURCES.startIcon());
        startBtn.setStyleName(RESOURCES.getCSS().leftBtn());
        startBtn.setTitle("Go to first page");
        startBtn.addClickHandler(event -> {
            currentRow = 0;
            changePage(currentRow);
            updateText();
        });

        previousBtn = new IconButton("", RESOURCES.previousIcon());
        previousBtn.setStyleName(RESOURCES.getCSS().leftBtn());
        previousBtn.setTitle("Go to previous page");
        previousBtn.addClickHandler( event -> {
            currentRow = currentRow - searchResult.getRows();
            changePage(currentRow);
            updateText();
        });

        nextBtn = new IconButton("", RESOURCES.nextIcon());
        nextBtn.setStyleName(RESOURCES.getCSS().rightBtn());
        nextBtn.setTitle("Go to next page");
        nextBtn.addClickHandler(event -> {
            currentRow = currentRow + searchResult.getRows();
            changePage(currentRow);
            updateText();
        });

        endBtn = new IconButton("", RESOURCES.endIcon());
        endBtn.setStyleName(RESOURCES.getCSS().rightBtn());
        endBtn.setTitle("Go to last page");
        endBtn.addClickHandler(event -> {
            currentRow = searchResult.getFound() - searchResult.getFound()%4 ;
            changePage(currentRow);
            updateText();
        });

        title = new Label(MSG);
        title.setStyleName(RESOURCES.getCSS().label());

        add(startBtn);
        add(previousBtn);
        add(endBtn);
        add(nextBtn);
        add(title);
    }

    private void updateText(){
        if(title != null) {
            title.setText(MSG + nf.format((currentRow/4 + 1)) + "/" + nf.format(totalPages));
        }
        startBtn.setEnabled(currentRow > 0);
        previousBtn.setEnabled(currentRow > 0);
        nextBtn.setEnabled(currentRow < (searchResult.getFound() - searchResult.getFound()%4) - 1);
        endBtn.setEnabled(currentRow < (searchResult.getFound() - searchResult.getFound()%4) - 1);
    }

    private void changePage(int newStartRow) {
        searchResult.setStartRow(newStartRow);
        fireEvent(new PageChangedEvent(searchResult));
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }
    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(SuggestionPanelCSS.CSS)
        SuggestionPanelCSS getCSS();

        @Source("images/left.png")
        ImageResource previousIcon();

        @Source("images/right.png")
        ImageResource nextIcon();

        @Source("images/start.png")
        ImageResource startIcon();

        @Source("images/end.png")
        ImageResource endIcon();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("fireworks-Pager")
    public interface SuggestionPanelCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/controls/common/pager/Pager.css";

        String mainPanel();

        String leftBtn();

        String rightBtn();

        String label();

    }
}
