/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
@Table(name = "MPCA_WEB_PAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaWebPage.findAll", query = "SELECT m FROM MpcaWebPage m"),
    @NamedQuery(name = "MpcaWebPage.findByPageId", query = "SELECT m FROM MpcaWebPage m WHERE m.pageId = :pageId"),
    @NamedQuery(name = "MpcaWebPage.findByPageName", query = "SELECT m FROM MpcaWebPage m WHERE m.pageName = :pageName"),
    @NamedQuery(name = "MpcaWebPage.findByPageUrl", query = "SELECT m FROM MpcaWebPage m WHERE m.pageUrl = :pageUrl")})
public class MpcaWebPage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PAGE_ID")
    private Long pageId;
    @Basic(optional = false)
    @Column(name = "PAGE_NAME")
    private String pageName;
    @Basic(optional = false)
    @Column(name = "PAGE_URL")
    private String pageUrl;
    @ManyToMany(mappedBy = "mpcaWebPageList", fetch = FetchType.LAZY)
    private List<MpcaProduct> mpcaProductList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pageId", fetch = FetchType.LAZY)
    private List<MpcaComment> mpcaCommentList;

    public MpcaWebPage() {
    }

    public MpcaWebPage(Long pageId) {
        this.pageId = pageId;
    }

    public MpcaWebPage(Long pageId, String pageName, String pageUrl) {
        this.pageId = pageId;
        this.pageName = pageName;
        this.pageUrl = pageUrl;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
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
    public List<MpcaProduct> getMpcaProductList() {
        return mpcaProductList;
    }

    public void setMpcaProductList(List<MpcaProduct> mpcaProductList) {
        this.mpcaProductList = mpcaProductList;
    }

    @XmlTransient
    public List<MpcaComment> getMpcaCommentList() {
        return mpcaCommentList;
    }

    public void setMpcaCommentList(List<MpcaComment> mpcaCommentList) {
        this.mpcaCommentList = mpcaCommentList;
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
        if (!(object instanceof MpcaWebPage)) {
            return false;
        }
        MpcaWebPage other = (MpcaWebPage) object;
        if ((this.pageId == null && other.pageId != null) || (this.pageId != null && !this.pageId.equals(other.pageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaWebPage[ pageId=" + pageId + " ]";
    }
    
}
