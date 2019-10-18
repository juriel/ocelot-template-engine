package net.comtor.ocelot.engine.commons.paginators;

import net.comtor.ocelot.html.HtmlContainer;
import net.comtor.ocelot.html.links.HtmlLi;
import net.comtor.ocelot.html.links.HtmlUl;
import net.comtor.ocelot.html.styles.HtmlDiv;
import net.comtor.ocelot.html.styles.HtmlSpan;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public class PaginatorBar extends HtmlDiv {

    private String urlEndPoint;
    private String idForm;
    private long totalRows;
    private long totalRowsInPage;
    private long totalPages;
    private long numIndex;
    private long maxNumRowsToView;
    private String messageResults;
    private String messageNext;
    private String messagePrevious;
    private String urlParams;

    public PaginatorBar(long numIndex, String idForm, String urlEndPoint,
            long totalPages, long totalRows, long pressedPage, long totalRowsInPage, long maxNumRowsToView) {
        initAll(urlEndPoint, idForm, totalRowsInPage, totalRows, totalPages,
                numIndex, maxNumRowsToView, pressedPage);
    }

    public PaginatorBar(long numIndex, String idForm, String urlEndPoint,
            long totalPages, long totalRows, long pressedPage, long totalRowsInPage,
            long maxNumRowsToView, String messageResults, String messageNext, String messagePrevious) {
        this.messageResults = messageResults;
        this.messageNext = messageNext;
        this.messagePrevious = messagePrevious;

        initAll(urlEndPoint, idForm, totalRowsInPage, totalRows, totalPages,
                numIndex, maxNumRowsToView, pressedPage);
    }

    private void initAll(String urlEndPoint1, String idForm1, long totalRowsInPage1,
            long totalRows1, long totalPages1, long numIndex1, long maxNumRowsToView1, long pressedPage) {
        this.urlEndPoint = urlEndPoint1;
        this.idForm = idForm1;
        this.totalRowsInPage = totalRowsInPage1;
        this.totalRows = totalRows1;
        this.totalPages = totalPages1;

        if (numIndex1 > 5) {
            this.numIndex = numIndex1;
        } else if (numIndex1 > totalPages1) {
            this.numIndex = this.totalPages;
        } else {
            this.numIndex = 5;
        }

        this.maxNumRowsToView = maxNumRowsToView1;
        initComponent(pressedPage);
    }

    public String getUrlParams() {
        return urlParams == null ? "" : urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }

    public void initComponent(long pressedPage) {
        this.addClass("dataTables_paginate paging_simple_numbers");
        this.setStyle("cursor: pointer; overflow:auto");

        HtmlUl pagContainer = new HtmlUl();
        pagContainer.addClass("pagination");
        pagContainer.add(getPreviusButton(pressedPage + 1));
        pagContainer.add(getPages(pressedPage));
        pagContainer.add(getNextButton(pressedPage + 1));

        long end = (long) ((double) maxNumRowsToView * (double) (pressedPage + 1)) - (maxNumRowsToView - totalRowsInPage);
        long init = end - (totalRowsInPage - 1);
        HtmlSpan span = new HtmlSpan(String.format((this.messageResults == null)
                ? "Mostrando registros de %1$s a %2$s de un total de %3$s"
                : this.messageResults, init, end, totalRows));
        span.setStyle("margin-left: 10px");
        pagContainer.add(span);

        this.add(pagContainer);
    }

    private HtmlLi getPreviusButton(long actualPage) {
        HtmlLi li = new HtmlLi();
        li.addClass("paginate_button previous page-item");

        if (actualPage == 1) {
            li.addClass("disabled");
        } else {
            if (actualPage <= numIndex) {
                String myUrl = urlEndPoint + "/0";
                li.addAttribute("id_form", idForm).addAttribute("endpoint", myUrl + getUrlParams());
            } else {
                String myUrl = urlEndPoint + "/" + (actualPage - 1 - numIndex);
                li.addAttribute("id_form", idForm).addAttribute("endpoint", myUrl + getUrlParams());
            }
        }

        HtmlSpan span = new HtmlSpan((this.messagePrevious == null)
                ? "Anterior"
                : this.messagePrevious);
        span.addClass("page-link");
        span.addAttribute("data-dt-idx", "0");
        span.addAttribute("tabindex", "0");

        li.add(span);

        return li;
    }

    private HtmlLi getNextButton(long actualPage) {
        HtmlLi li = new HtmlLi();
        li.addClass("paginate_button next");

        if (actualPage == totalPages || totalRows == 0) {
            li.addClass("disabled");
        } else {
            if ((actualPage + numIndex) > totalPages) {
                String myUrl = urlEndPoint + "/" + (totalPages - 1);
                li.addAttribute("id_form", idForm).addAttribute("endpoint", myUrl + getUrlParams());
            } else {
                String myUrl = urlEndPoint + "/" + (actualPage - 1 + numIndex);
                li.addAttribute("id_form", idForm).addAttribute("endpoint", myUrl + getUrlParams());
            }
        }

        HtmlSpan span = new HtmlSpan((this.messageNext == null)
                ? "Siguiente"
                : this.messageNext);
        span.addClass("page-link");
        span.addAttribute("data-dt-idx", "6");
        span.addAttribute("tabindex", "0");

        li.add(span);

        return li;
    }

    private HtmlLi getPage(boolean isActive, int dataIdx, long pageNumber) {
        HtmlLi li = new HtmlLi();
        li.addClass("paginate_button");

        if (isActive) {
            li.addClass("active");
        }

        HtmlSpan span = new HtmlSpan(pageNumber + "");
        span.addClass("page-link" + (isActive ? " text-white" : ""));
        span.addAttribute("data-dt-idx", dataIdx + "");
        span.addAttribute("tabindex", "0");
        li.add(span);

        String myUrl = urlEndPoint + "/" + (pageNumber - 1);
        li.addAttribute("id_form", idForm)
                .addAttribute("endpoint", myUrl + getUrlParams());

        return li;
    }

    private HtmlContainer getPages(long actualPage) {
        HtmlContainer container = new HtmlContainer();
        long numPage = this.totalPages;
        long minPage = 1;

        actualPage++;

        long referencePoint = (long) (((double) numIndex / 2) + 1);

        if (actualPage > referencePoint) {
            minPage = (actualPage - (referencePoint - 1));
        }

        int x = 0;

        for (long i = minPage; i <= numPage; i++) {
            x++;

            if (x > numIndex) {
                break;
            }

            container.add(getPage((i == actualPage), x, i));
        }

        return container;
    }

}
