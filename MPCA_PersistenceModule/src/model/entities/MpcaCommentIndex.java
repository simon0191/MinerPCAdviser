/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "MPCA_COMMENT_INDEX")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaCommentIndex.findAll", query = "SELECT m FROM MpcaCommentIndex m"),
    @NamedQuery(name = "MpcaCommentIndex.findByMpcaIndexTypeIndexId", query = "SELECT m FROM MpcaCommentIndex m WHERE m.mpcaCommentIndexPK.mpcaIndexTypeIndexId = :mpcaIndexTypeIndexId"),
    @NamedQuery(name = "MpcaCommentIndex.findByMpcaCommentCommentId", query = "SELECT m FROM MpcaCommentIndex m WHERE m.mpcaCommentIndexPK.mpcaCommentCommentId = :mpcaCommentCommentId")})
public class MpcaCommentIndex implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MpcaCommentIndexPK mpcaCommentIndexPK;
    @JoinColumn(name = "LABEL_ID", referencedColumnName = "LABEL_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaLabelType labelId;
    @JoinColumn(name = "MPCA_INDEX_TYPE_INDEX_ID", referencedColumnName = "INDEX_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaIndexType mpcaIndexType;
    @JoinColumn(name = "MPCA_COMMENT_COMMENT_ID", referencedColumnName = "COMMENT_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaComment mpcaComment;

    public MpcaCommentIndex() {
    }

    public MpcaCommentIndex(MpcaCommentIndexPK mpcaCommentIndexPK) {
        this.mpcaCommentIndexPK = mpcaCommentIndexPK;
    }

    public MpcaCommentIndex(long mpcaIndexTypeIndexId, long mpcaCommentCommentId) {
        this.mpcaCommentIndexPK = new MpcaCommentIndexPK(mpcaIndexTypeIndexId, mpcaCommentCommentId);
    }

    public MpcaCommentIndexPK getMpcaCommentIndexPK() {
        return mpcaCommentIndexPK;
    }

    public void setMpcaCommentIndexPK(MpcaCommentIndexPK mpcaCommentIndexPK) {
        this.mpcaCommentIndexPK = mpcaCommentIndexPK;
    }

    public MpcaLabelType getLabelId() {
        return labelId;
    }

    public void setLabelId(MpcaLabelType labelId) {
        this.labelId = labelId;
    }

    public MpcaIndexType getMpcaIndexType() {
        return mpcaIndexType;
    }

    public void setMpcaIndexType(MpcaIndexType mpcaIndexType) {
        this.mpcaIndexType = mpcaIndexType;
    }

    public MpcaComment getMpcaComment() {
        return mpcaComment;
    }

    public void setMpcaComment(MpcaComment mpcaComment) {
        this.mpcaComment = mpcaComment;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpcaCommentIndexPK != null ? mpcaCommentIndexPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaCommentIndex)) {
            return false;
        }
        MpcaCommentIndex other = (MpcaCommentIndex) object;
        if ((this.mpcaCommentIndexPK == null && other.mpcaCommentIndexPK != null) || (this.mpcaCommentIndexPK != null && !this.mpcaCommentIndexPK.equals(other.mpcaCommentIndexPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaCommentIndex[ mpcaCommentIndexPK=" + mpcaCommentIndexPK + " ]";
    }
    
}
