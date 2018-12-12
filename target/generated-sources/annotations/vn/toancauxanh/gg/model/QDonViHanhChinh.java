package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDonViHanhChinh is a Querydsl query type for DonViHanhChinh
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDonViHanhChinh extends EntityPathBase<DonViHanhChinh> {

    private static final long serialVersionUID = 2110646134L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QDonViHanhChinh donViHanhChinh = new QDonViHanhChinh("donViHanhChinh");

    public final vn.toancauxanh.model.QModel _super;

    public final NumberPath<Integer> capDonVi = createNumber("capDonVi", Integer.class);

    public final QDonViHanhChinh cha;

    public final NumberPath<Long> danSo = createNumber("danSo", Long.class);

    //inherited
    public final BooleanPath daXoa;

    //inherited
    public final NumberPath<Long> id;

    public final StringPath ma = createString("ma");

    public final BooleanPath macDinh = createBoolean("macDinh");

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final NumberPath<Integer> soThuTu = createNumber("soThuTu", Integer.class);

    public final StringPath ten = createString("ten");

    public final BooleanPath thanhThi = createBoolean("thanhThi");

    //inherited
    public final StringPath trangThai;

    public QDonViHanhChinh(String variable) {
        this(DonViHanhChinh.class, forVariable(variable), INITS);
    }

    public QDonViHanhChinh(Path<? extends DonViHanhChinh> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDonViHanhChinh(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDonViHanhChinh(PathMetadata metadata, PathInits inits) {
        this(DonViHanhChinh.class, metadata, inits);
    }

    public QDonViHanhChinh(Class<? extends DonViHanhChinh> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new vn.toancauxanh.model.QModel(type, metadata, inits);
        this.cha = inits.isInitialized("cha") ? new QDonViHanhChinh(forProperty("cha"), inits.get("cha")) : null;
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
    }

}

