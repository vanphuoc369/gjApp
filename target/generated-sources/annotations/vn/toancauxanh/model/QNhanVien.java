package vn.toancauxanh.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNhanVien is a Querydsl query type for NhanVien
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNhanVien extends EntityPathBase<NhanVien> {

    private static final long serialVersionUID = 755098746L;

    private static final PathInits INITS = new PathInits("*", "donVi.*.*.*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QNhanVien nhanVien = new QNhanVien("nhanVien");

    public final QModel _super;

    public final BooleanPath checkKichHoat = createBoolean("checkKichHoat");

    public final StringPath chucVu = createString("chucVu");

    //inherited
    public final BooleanPath daXoa;

    public final StringPath diaChi = createString("diaChi");

    public final vn.toancauxanh.gg.model.QDonVi donVi;

    public final vn.toancauxanh.gg.model.QDonVi donViCha;

    public final vn.toancauxanh.gg.model.QDonVi donViCon;

    public final StringPath email = createString("email");

    public final StringPath hinhDaiDien = createString("hinhDaiDien");

    public final StringPath hoVaTen = createString("hoVaTen");

    //inherited
    public final NumberPath<Long> id;

    public final StringPath matKhau = createString("matKhau");

    public final DateTimePath<java.util.Date> ngaySinh = createDateTime("ngaySinh", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final QNhanVien nguoiSua;

    // inherited
    public final QNhanVien nguoiTao;

    public final SetPath<String, StringPath> quyens = this.<String, StringPath>createSet("quyens", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath salkey = createString("salkey");

    public final BooleanPath selectedDV = createBoolean("selectedDV");

    public final StringPath soDienThoai = createString("soDienThoai");

    public final StringPath tenDangNhap = createString("tenDangNhap");

    //inherited
    public final StringPath trangThai;

    public final SetPath<VaiTro, QVaiTro> vaiTros = this.<VaiTro, QVaiTro>createSet("vaiTros", VaiTro.class, QVaiTro.class, PathInits.DIRECT2);

    public QNhanVien(String variable) {
        this(NhanVien.class, forVariable(variable), INITS);
    }

    public QNhanVien(Path<? extends NhanVien> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNhanVien(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNhanVien(PathMetadata metadata, PathInits inits) {
        this(NhanVien.class, metadata, inits);
    }

    public QNhanVien(Class<? extends NhanVien> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.donVi = inits.isInitialized("donVi") ? new vn.toancauxanh.gg.model.QDonVi(forProperty("donVi"), inits.get("donVi")) : null;
        this.donViCha = inits.isInitialized("donViCha") ? new vn.toancauxanh.gg.model.QDonVi(forProperty("donViCha"), inits.get("donViCha")) : null;
        this.donViCon = inits.isInitialized("donViCon") ? new vn.toancauxanh.gg.model.QDonVi(forProperty("donViCon"), inits.get("donViCon")) : null;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
    }

}

