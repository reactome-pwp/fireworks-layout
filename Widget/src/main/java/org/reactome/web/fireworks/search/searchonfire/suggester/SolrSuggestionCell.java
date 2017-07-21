package org.reactome.web.fireworks.search.searchonfire.suggester;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Image;
import org.reactome.web.fireworks.model.Node;
import org.reactome.web.fireworks.search.searchonfire.solr.model.Entry;
import org.reactome.web.fireworks.util.SearchResultImageMapper;

import static org.reactome.web.fireworks.util.SearchResultImageMapper.ImageContainer;

/**
 * A custom {@link Cell} used to render the suggestion for a {@link Node}
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SolrSuggestionCell extends AbstractCell<Entry> {

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
//        /**
//         * The template for this Cell, which includes styles and a value.
//         *
//         * @param styles the styles to include in the style attribute of the div
//         * @param value  the safe value. Since the value type is {@link SafeHtml},
//         *               it will not be escaped before including it in the template.
//         *               Alternatively, you could make the value type String, in which
//         *               case the value would be escaped.
//         * @return a {@link SafeHtml} instance
//         */
//        @SafeHtmlTemplates.Template("<div style=\"{0}\">{1}</div>")
//        SafeHtml _cell(SafeStyles styles, SafeHtml value);

        @Template("<div title=\"{2}\" style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis;\">&nbsp;&nbsp;{0}&nbsp;&nbsp;<span>{1}</span></div>")
        SafeHtml minCell(SafeHtml image, SafeHtml value, String tooltip);

        @SafeHtmlTemplates.Template("" +
                "<div style=\"overflow:hidden; width:100%;\">" +
                    "<div title=\"{1}\" style=\"float:left;margin: 7px 0 0 5px\">{0}</div>" +
                    "<div title=\"{4}\" style=\"float:left;margin-left:10px; max-width:260px\">" +
                        "<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; font-size:small\">" +
                            "{2}" +
                        "</div>" +
                        "<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; margin-top:-2px; font-size:x-small;\">" +
                            "{3}" +
                        "</div>" +
                    "</div>" +
                "</div>")
        SafeHtml cell(SafeHtml image, String imgTooltip, SafeHtml primary, SafeHtml secondary, String tooltip);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);


    @Override
    public void render(Context context, Entry value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        ImageContainer imageContainer = SearchResultImageMapper.getImage(value.getExactType());

        Image image = new Image(imageContainer.getImageResource());
        SafeHtml safeImage = SafeHtmlUtils.fromTrustedString(image.toString());
        SafeHtml primary = SafeHtmlUtils.fromTrustedString(value.getName());

        if (value.getStId()==null || value.getStId().isEmpty()) {
            sb.append(templates.minCell(safeImage, primary, imageContainer.getTooltip()));
        } else {
            SafeHtml secondary = SafeHtmlUtils.fromTrustedString(value.getStId());
            sb.append(templates.cell(safeImage, imageContainer.getTooltip(), primary, secondary, value.getName()));
        }
    }
}
