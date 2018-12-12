package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDonVi is a Querydsl query type for DonVi
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDonVi extends EntityPathBase<DonVi> {

    private static final long serialVersionUID = 1748486325L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QDonVi donVi = new QDonVi("donVi");

    public final vn.toancauxanh.model.QModel _super;

    public final QDonVi cha;

    //inherited
    public final BooleanPath daXoa;

    public final QDonViHanhChinh donViHanhChinh;

    //inherited
    public final NumberPath<Long> id;

    public final StringPath moTa = createString("moTa");

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final QDonViHanhChinh phuong;

    public final QDonViHanhChinh quan;

    public final StringPath ten = createString("ten");

    public final QDonViHanhChinh thanhPho;

    //inherited
    public final StringPath trangThai;

    public QDonVi(String variable) {
        this(DonVi.class, forVariable(variable), INITS);
    }

    public QDonVi(Path<? extends DonVi> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDonVi(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDonVi(PathMetadata metadata, PathInits inits) {
        this(DonVi.class, metadata, inits);
    }

    public QDonVi(Class<? extends DonVi> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new vn.toancauxanh.model.QModel(type, metadata, inits);
        this.cha = inits.isInitialized("cha") ? new QDonVi(forProperty("cha"), inits.get("cha")) : null;
        this.daXoa = _super.daXoa;
        this.donViHanhChinh = inits.isInitialized("donViHanhChinh") ? new QDonViHanhChinh(forProperty("donViHanhChinh"), inits.get("donViHanhChinh")) : null;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.phuong = inits.isInitialized("phuong") ? new QDonViHanhChinh(forProperty("phuong"), inits.get("phuong")) : null;
        this.quan = inits.isInitialized("quan") ? new QDonViHanhChinh(forProperty("quan"), inits.get("quan")) : null;
        this.thanhPho = inits.isInitialized("thanhPho") ? new QDonViHanhChinh(forProperty("thanhPho"), inits.get("thanhPho")) : null;
        this.trangThai = _super.trangThai;
    }

}

