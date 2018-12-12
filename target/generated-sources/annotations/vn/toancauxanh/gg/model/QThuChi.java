package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QThuChi is a Querydsl query type for ThuChi
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QThuChi extends EntityPathBase<ThuChi> {

    private static final long serialVersionUID = -1179706652L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QThuChi thuChi = new QThuChi("thuChi");

    public final vn.toancauxanh.model.QModel _super;

    public final BooleanPath daNop = createBoolean("daNop");

    //inherited
    public final BooleanPath daXoa;

    //inherited
    public final NumberPath<Long> id;

    public final QLyDoThuChi lyDoThuChi;

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    public final vn.toancauxanh.model.QNhanVien nguoiNop;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final NumberPath<Integer> soTienNop = createNumber("soTienNop", Integer.class);

    //inherited
    public final StringPath trangThai;

    public QThuChi(String variable) {
        this(ThuChi.class, forVariable(variable), INITS);
    }

    public QThuChi(Path<? extends ThuChi> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QThuChi(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QThuChi(PathMetadata metadata, PathInits inits) {
        this(ThuChi.class, metadata, inits);
    }

    public QThuChi(Class<? extends ThuChi> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new vn.toancauxanh.model.QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.lyDoThuChi = inits.isInitialized("lyDoThuChi") ? new QLyDoThuChi(forProperty("lyDoThuChi"), inits.get("lyDoThuChi")) : null;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiNop = inits.isInitialized("nguoiNop") ? new vn.toancauxanh.model.QNhanVien(forProperty("nguoiNop"), inits.get("nguoiNop")) : null;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
    }

}

