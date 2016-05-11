package org.reactome.web.fireworks.util;

import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionPanel;
import org.reactome.web.pwp.model.images.DatabaseObjectImages;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public abstract class ImageResolver {

    public static ImageResource getImage(String type) {
        ImageResource rtn;
        if (type != null) {
            switch (type.toLowerCase()) {
                case "reaction":
                    rtn = DatabaseObjectImages.INSTANCE.reaction();
                    break;
                case "protein":
                    rtn = DatabaseObjectImages.INSTANCE.entityWithAccessionedSequence();
                    break;
                case "complex":
                    rtn = DatabaseObjectImages.INSTANCE.complex();
                    break;
                case "set":
                    rtn = DatabaseObjectImages.INSTANCE.entitySet();
                    break;
                case "interactor":
                    rtn = SolrSuggestionPanel.RESOURCES.interactor();
                    break;
                case "pathway":
                    rtn = DatabaseObjectImages.INSTANCE.pathway();
                    break;
                case "genes and transcripts":
                    rtn = DatabaseObjectImages.INSTANCE.genomeEncodeEntity();
                    break;
                case "dna sequence":
                    rtn = DatabaseObjectImages.INSTANCE.referenceDNASequence();
                    break;
                case "polymer":
                    rtn = DatabaseObjectImages.INSTANCE.polymer();
                    break;
                case "rna sequence":
                    rtn = DatabaseObjectImages.INSTANCE.referenceRNASequence();
                    break;
                default:
                    //TODO: :)
                    rtn = DatabaseObjectImages.INSTANCE.pathway();
                    break;
            }
        } else {
            //TODO: :)
            rtn = DatabaseObjectImages.INSTANCE.pathway();
        }
        return rtn;
    }
}
