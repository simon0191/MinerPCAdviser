/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SimonXPS
 */
@Entity
@Table(name = "MPCA_COMMENT_INDEX")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaCommentIndex.findAll", query = "SELECT m FROM MpcaCommentIndex m"),
    @NamedQuery(name = "MpcaCommentIndex.findByIndexValue", query = "SELECT m FROM MpcaCommentIndex m WHERE m.indexValue = :indexValue"),
    @NamedQuery(name = "MpcaCommentIndex.findByCommentIndexId", query = "SELECT m FROM MpcaCommentIndex m WHERE m.commentIndexId = :commentIndexId"),
    @NamedQuery(name = "MpcaCommentIndex.DeleteByIndexTypeId", query = "DELETE FROM MpcaCommentIndex c WHERE c.mpcaIndexTypeIndexId.indexId = :indexTypeId")
})
public class MpcaCommentIndex implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "INDEX_VALUE")
    private BigDecimal indexValue;
    @Id
    @Basic(optional = false)
    @Column(name = "COMMENT_INDEX_ID")
    @SequenceGenerator(name="SEQ_COMMENT_INDEX", sequenceName = "MPCA_COMMENT_INDEX_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMMENT_INDEX")
    private Long commentIndexId;
    @JoinColumn(name = "LABEL_ID", referencedColumnName = "LABEL_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaLabelType labelId;
    @JoinColumn(name = "MPCA_INDEX_TYPE_INDEX_ID", referencedColumnName = "INDEX_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaIndexType mpcaIndexTypeIndexId;
    @JoinColumn(name = "MPCA_COMMENT_COMMENT_ID", referencedColumnName = "COMMENT_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaComment mpcaCommentCommentId;

    public MpcaCommentIndex() {
    }

    public MpcaCommentIndex(Long commentIndexId) {
        this.commentIndexId = commentIndexId;
    }

    public MpcaCommentIndex(Long commentIndexId, BigDecimal indexValue) {
        this.commentIndexId = commentIndexId;
        this.indexValue = indexValue;
    }

    public BigDecimal getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(BigDecimal indexValue) {
        this.indexValue = indexValue;
    }

    public Long getCommentIndexId() {
        return commentIndexId;
    }

    public void setCommentIndexId(Long commentIndexId) {
        this.commentIndexId = commentIndexId;
    }

    public MpcaLabelType getLabelId() {
        return labelId;
    }

    public void setLabelId(MpcaLabelType labelId) {
        this.labelId = labelId;
    }

    public MpcaIndexType getMpcaIndexTypeIndexId() {
        return mpcaIndexTypeIndexId;
    }

    public void setMpcaIndexTypeIndexId(MpcaIndexType mpcaIndexTypeIndexId) {
        this.mpcaIndexTypeIndexId = mpcaIndexTypeIndexId;
    }

    public MpcaComment getMpcaCommentCommentId() {
        return mpcaCommentCommentId;
    }

    public void setMpcaCommentCommentId(MpcaComment mpcaCommentCommentId) {
        this.mpcaCommentCommentId = mpcaCommentCommentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (commentIndexId != null ? commentIndexId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaCommentIndex)) {
            return false;
        }
        MpcaCommentIndex other = (MpcaCommentIndex) object;
        if ((this.commentIndexId == null && other.commentIndexId != null) || (this.commentIndexId != null && !this.commentIndexId.equals(other.commentIndexId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaCommentIndex[ commentIndexId=" + commentIndexId + " ]";
    }
    
}
