package org.reactome.web.test;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.http.client.*;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.client.FireworksViewer;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WidgetTest implements EntryPoint {

    private String currentSpecies;

    /**
     * Only used for testing purposes
     * It subscribe to onClick and onMouseOver to simulate the future Hierarchy tree
     */
    private class TestButton extends Button {

        TestButton(String html, final String stId, final FireworksViewer viewer) {
            super(html, new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    viewer.resetHighlight();
                    viewer.selectNode(stId);
                }
            });

            this.addMouseOverHandler(new MouseOverHandler() {
                @Override
                public void onMouseOver(MouseOverEvent event) {
                    viewer.highlightNode(stId);
                }
            });

            this.addMouseOutHandler(new MouseOutHandler() {
                @Override
                public void onMouseOut(MouseOutEvent event) {
                    viewer.resetHighlight();
                }
            });
        }
    }

    @Override
    public void onModuleLoad() {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
            }

            @SuppressWarnings("unchecked")
            public void onSuccess() {
                FireworksFactory.CONSOLE_VERBOSE = true;
                FireworksFactory.EVENT_BUS_VERBOSE = true;
//                FireworksFactory.SHOW_INFO = true;

                loadSpeciesFireworks("Homo_sapiens");
//                loadSpeciesFireworks("Oryza_sativa");
//                loadSpeciesFireworks("Gallus_gallus");
//                loadSpeciesFireworks("Mycobacterium_tuberculosis");
//                loadSpeciesFireworks("Mus_musculus");
//                loadSpeciesFireworks("Saccharomyces_cerevisiae");
//                loadSpeciesFireworks("Sus_scrofa");

            }
        });
    }

    public void initialise(String json){
        final FireworksViewer fireworks = FireworksFactory.createFireworksViewer(json);

        VerticalPanel vp = new VerticalPanel();
        vp.add(new TestButton("TRP", "R-HSA-3295583", fireworks));
        vp.add(new TestButton("RAF/MAP", "R-HSA-5673001", fireworks));
        vp.add(new TestButton("ERK", "R-HSA-112409", fireworks));
        vp.add(new TestButton("Hexose", "R-HSA-189200", fireworks));
        vp.add(new TestButton("Regu..", "R-HSA-169911", fireworks));
        vp.add(new TestButton("Repro..", "R-HSA-1474165", fireworks));
        vp.add(new TestButton("Striated", "R-HSA-390522", fireworks));

        FlowPanel fp = new FlowPanel();
        fp.add(new Button("Reload Fireworks", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadSpeciesFireworks(currentSpecies);
            }
        }));
        fp.add(new Button("OVERREPRESENTATION", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fireworks.setAnalysisToken("MjAxNjExMjQxMDE5NTZfNg==", "TOTAL");
            }
        }));
        fp.add(new Button("EXPRESSION", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fireworks.setAnalysisToken("MjAxNjAyMDkxMTQxMzRfMg%253D%253D", "TOTAL");
            }
        }));
        fp.add(new Button("EXPRESSION 2", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fireworks.setAnalysisToken("MjAxNjAyMjkwODQzMjZfNw%253D%253D", "TOTAL");
            }
        }));
        fp.add(new Button("SPECIES COMPARISON", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fireworks.setAnalysisToken(URL.decode("MjAxNjAyMjkwNjU4MDNfNQ%253D%253D"), "TOTAL");
            }
        }));


        SplitLayoutPanel slp = new SplitLayoutPanel(10);
        slp.addWest(vp, 80);
        slp.addSouth(fp, 50);
        slp.add(fireworks);

        RootLayoutPanel.get().clear();
        RootLayoutPanel.get().add(slp);

//        fireworks.flagItems("15422");
        fireworks.flagNodes("Reactions and Boxes Mixed", getPathways(TestSource.SOURCE.reactionsAndBoxesMixed().getText()));

    }

    public void loadSpeciesFireworks(String species){
        this.currentSpecies = species;
        String url = "/download/current/fireworks/" + species + ".json?v=" + System.currentTimeMillis();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        initialise(response.getText());
                    }catch (Exception ex){
                        if(!GWT.isScript()) ex.printStackTrace();
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()) exception.printStackTrace();
                }
            });
        }catch (RequestException ex) {
            if(!GWT.isScript()) ex.printStackTrace();
        }

    }

    private String[] getPathways(String text){
        return text.split("\\n");
    }

    interface TestSource extends ClientBundle {

        WidgetTest.TestSource SOURCE = GWT.create(WidgetTest.TestSource.class);

        @Source("PathwayDOI.csv")
        TextResource pathwayDOI();

        @Source("EHLD_targets.csv")
        TextResource ehldTargets();

        @Source("MixedReactionsGreenBoxes.csv")
        TextResource reactionsAndBoxesMixed();

    }
}
