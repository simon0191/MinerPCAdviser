/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "WEB_PAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WebPage.findAll", query = "SELECT w FROM WebPage w"),
    @NamedQuery(name = "WebPage.findByPageId", query = "SELECT w FROM WebPage w WHERE w.pageId = :pageId"),
    @NamedQuery(name = "WebPage.findByPageName", query = "SELECT w FROM WebPage w WHERE w.pageName = :pageName"),
    @NamedQuery(name = "WebPage.findByPageUrl", query = "SELECT w FROM WebPage w WHERE w.pageUrl = :pageUrl")})
public class WebPage implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PAGE_ID")
    private BigDecimal pageId;
    @Basic(optional = false)
    @Column(name = "PAGE_NAME")
    private String pageName;
    @Basic(optional = false)
    @Column(name = "PAGE_URL")
    private String pageUrl;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "webPage", fetch = FetchType.LAZY)
    private List<MpcaComment> mpcaCommentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "webPage", fetch = FetchType.LAZY)
    private List<ProductWebPage> productWebPageList;

    public WebPage() {
    }

    public WebPage(String pageName, String pageUrl) {
        this.pageName = pageName;
        this.pageUrl = pageUrl;
    }

    public WebPage(BigDecimal pageId) {
        this.pageId = pageId;
    }

    public WebPage(BigDecimal pageId, String pageName, String pageUrl) {
        this.pageId = pageId;
        this.pageName = pageName;
        this.pageUrl = pageUrl;
    }

    public BigDecimal getPageId() {
        return pageId;
    }

    public void setPageId(BigDecimal pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    @XmlTransient
    public List<MpcaComment> getMpcaCommentList() {
        return mpcaCommentList;
    }

    public void setMpcaCommentList(List<MpcaComment> mpcaCommentList) {
        this.mpcaCommentList = mpcaCommentList;
    }

    @XmlTransient
    public List<ProductWebPage> getProductWebPageList() {
        return productWebPageList;
    }

    public void setProductWebPageList(List<ProductWebPage> productWebPageList) {
        this.productWebPageList = productWebPageList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pageId != null ? pageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WebPage)) {
            return false;
        }
        WebPage other = (WebPage) object;
        if ((this.pageId == null && other.pageId != null) || (this.pageId != null && !this.pageId.equals(other.pageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.WebPage[ pageId=" + pageId + " ]";
    }
    
}
