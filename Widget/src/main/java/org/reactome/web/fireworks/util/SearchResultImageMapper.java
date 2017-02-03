package org.reactome.web.fireworks.util;

import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionPanel;
import org.reactome.web.pwp.model.images.DatabaseObjectImages;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public abstract class SearchResultImageMapper {

    public static ImageResource getImage(String type) {
        if (type != null) {
            switch (type.toLowerCase()) {
                case "reaction":
                case "failedreaction":
                case "blackboxevent":
                case "polymerisation":
                case "depolimerisation":
                    return DatabaseObjectImages.INSTANCE.reaction();
                case "genomeencodedentity":
                    return DatabaseObjectImages.INSTANCE.genomeEncodeEntity();
                case "protein":
                case "referencegeneproduct":
                    return DatabaseObjectImages.INSTANCE.entityWithAccessionedSequence();
                case "complex":
                    return DatabaseObjectImages.INSTANCE.complex();
                case "set":
                case "candidateset":
                case "definedset":
                case "openset":
                    return DatabaseObjectImages.INSTANCE.entitySet();
                case "interactor":
                    return SolrSuggestionPanel.RESOURCES.interactor();
                case "pathway":
                case "toplevelpathway":
                    return DatabaseObjectImages.INSTANCE.pathway();
                case "genes and transcripts":
                    return DatabaseObjectImages.INSTANCE.genomeEncodeEntity();
                case "dna sequence":
                case "referencednasequence":
                    return DatabaseObjectImages.INSTANCE.referenceDNASequence();
                case "polymer":
                    return DatabaseObjectImages.INSTANCE.polymer();
                case "rna sequence":
                case "referencernasequence":
                    return DatabaseObjectImages.INSTANCE.referenceRNASequence();
                case "regulation":
                case "requirement":
                case "positiveregulation":
                case "negativeregulation":
                    return DatabaseObjectImages.INSTANCE.regulator();
                case "chemical compound":
                case "referencemolecule":
                    return DatabaseObjectImages.INSTANCE.simpleEntity();
                case "otherentity":
                    return DatabaseObjectImages.INSTANCE.otherEntity();
                default:
                    return DatabaseObjectImages.INSTANCE.exclamation();
            }
        } else {
            return DatabaseObjectImages.INSTANCE.exclamation();
        }
    }
}
