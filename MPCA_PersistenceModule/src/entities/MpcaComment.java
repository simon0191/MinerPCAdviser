/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "MPCA_COMMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaComment.findAll", query = "SELECT m FROM MpcaComment m"),
    @NamedQuery(name = "MpcaComment.findByCommentId", query = "SELECT m FROM MpcaComment m WHERE m.mpcaCommentPK.commentId = :commentId"),
    @NamedQuery(name = "MpcaComment.findByPublicationDate", query = "SELECT m FROM MpcaComment m WHERE m.publicationDate = :publicationDate"),
    @NamedQuery(name = "MpcaComment.findByCommentUrl", query = "SELECT m FROM MpcaComment m WHERE m.commentUrl = :commentUrl"),
    @NamedQuery(name = "MpcaComment.findByPageId", query = "SELECT m FROM MpcaComment m WHERE m.mpcaCommentPK.pageId = :pageId"),
    @NamedQuery(name = "MpcaComment.findByProductId", query = "SELECT m FROM MpcaComment m WHERE m.mpcaCommentPK.productId = :productId"),
    @NamedQuery(name = "MpcaComment.findByAuthorId", query = "SELECT m FROM MpcaComment m WHERE m.mpcaCommentPK.authorId = :authorId"),
    @NamedQuery(name = "MpcaComment.findByBrandId", query = "SELECT m FROM MpcaComment m WHERE m.mpcaCommentPK.brandId = :brandId"),
    @NamedQuery(name = "MpcaComment.findByAdditionAndValue", query = "SELECT c FROM MpcaComment c INNER JOIN c.commentAdditionList a INNER JOIN Addition aa WHERE a.value = :value AND aa.addType = :addType")
})
public class MpcaComment implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MpcaCommentPK mpcaCommentPK;
    @Basic(optional = false)
    @Lob
    @Column(name = "COMMENT_TEXT")
    private String commentText;
    @Basic(optional = false)
    @Column(name = "PUBLICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publicationDate;
    @Basic(optional = false)
    @Column(name = "COMMENT_URL")
    private String commentUrl;
    @JoinColumn(name = "PAGE_ID", referencedColumnName = "PAGE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private WebPage webPage;
    @JoinColumns({
        @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = false, updatable = false),
        @JoinColumn(name = "BRAND_ID", referencedColumnName = "BRAND_ID", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product product;
    @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "AUTHOR_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Author author;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mpcaComment", fetch = FetchType.LAZY)
    private List<CommentAddition> commentAdditionList;

    public MpcaComment() {
    }

    public MpcaComment(MpcaCommentPK mpcaCommentPK) {
        this.mpcaCommentPK = mpcaCommentPK;
    }

    public MpcaComment(MpcaCommentPK mpcaCommentPK, String commentText, Date publicationDate, String commentUrl) {
        this.mpcaCommentPK = mpcaCommentPK;
        this.commentText = commentText;
        this.publicationDate = publicationDate;
        this.commentUrl = commentUrl;
    }

    public MpcaComment(BigInteger commentId, BigInteger pageId, BigInteger productId, BigInteger authorId, BigInteger brandId) {
        this.mpcaCommentPK = new MpcaCommentPK(commentId, pageId, productId, authorId, brandId);
    }

    public MpcaCommentPK getMpcaCommentPK() {
        return mpcaCommentPK;
    }

    public void setMpcaCommentPK(MpcaCommentPK mpcaCommentPK) {
        this.mpcaCommentPK = mpcaCommentPK;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    public WebPage getWebPage() {
        return webPage;
    }

    public void setWebPage(WebPage webPage) {
        this.webPage = webPage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @XmlTransient
    public List<CommentAddition> getCommentAdditionList() {
        return commentAdditionList;
    }

    public void setCommentAdditionList(List<CommentAddition> commentAdditionList) {
        this.commentAdditionList = commentAdditionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpcaCommentPK != null ? mpcaCommentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaComment)) {
            return false;
        }
        MpcaComment other = (MpcaComment) object;
        if ((this.mpcaCommentPK == null && other.mpcaCommentPK != null) || (this.mpcaCommentPK != null && !this.mpcaCommentPK.equals(other.mpcaCommentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaComment[ mpcaCommentPK=" + mpcaCommentPK + ", commentText = " + commentText + " ]";
    }
    
}
