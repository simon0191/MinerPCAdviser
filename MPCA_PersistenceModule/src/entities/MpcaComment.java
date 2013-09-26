/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    @NamedQuery(name = "MpcaComment.findByCommentId", query = "SELECT m FROM MpcaComment m WHERE m.commentId = :commentId"),
    @NamedQuery(name = "MpcaComment.findByPublicationDate", query = "SELECT m FROM MpcaComment m WHERE m.publicationDate = :publicationDate"),
    @NamedQuery(name = "MpcaComment.findByAdditionAndValue", query = "SELECT ca FROM MpcaCommentAddition ca WHERE ca.mpcaAdditionType.addType = :addType AND ca.value = :value")
})
public class MpcaComment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COMMENT_ID")
    private Long commentId;
    @Basic(optional = false)
    @Lob
    @Column(name = "COMMENT_TEXT")
    private String commentText;
    @Basic(optional = false)
    @Column(name = "PUBLICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publicationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mpcaComment", fetch = FetchType.LAZY)
    private List<MpcaCommentIndex> mpcaCommentIndexList;
    @JoinColumn(name = "PAGE_ID", referencedColumnName = "PAGE_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaWebPage pageId;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaProduct productId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mpcaComment", fetch = FetchType.LAZY)
    private List<MpcaCommentAddition> mpcaCommentAdditionList;

    public MpcaComment() {
    }

    public MpcaComment(Long commentId) {
        this.commentId = commentId;
    }

    public MpcaComment(Long commentId, String commentText, Date publicationDate) {
        this.commentId = commentId;
        this.commentText = commentText;
        this.publicationDate = publicationDate;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
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

    @XmlTransient
    public List<MpcaCommentIndex> getMpcaCommentIndexList() {
        return mpcaCommentIndexList;
    }

    public void setMpcaCommentIndexList(List<MpcaCommentIndex> mpcaCommentIndexList) {
        this.mpcaCommentIndexList = mpcaCommentIndexList;
    }

    public MpcaWebPage getPageId() {
        return pageId;
    }

    public void setPageId(MpcaWebPage pageId) {
        this.pageId = pageId;
    }

    public MpcaProduct getProductId() {
        return productId;
    }

    public void setProductId(MpcaProduct productId) {
        this.productId = productId;
    }

    @XmlTransient
    public List<MpcaCommentAddition> getMpcaCommentAdditionList() {
        return mpcaCommentAdditionList;
    }

    public void setMpcaCommentAdditionList(List<MpcaCommentAddition> mpcaCommentAdditionList) {
        this.mpcaCommentAdditionList = mpcaCommentAdditionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (commentId != null ? commentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaComment)) {
            return false;
        }
        MpcaComment other = (MpcaComment) object;
        if ((this.commentId == null && other.commentId != null) || (this.commentId != null && !this.commentId.equals(other.commentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaComment[ commentId=" + commentId + " ]";
    }
    
}
