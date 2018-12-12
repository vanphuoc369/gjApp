package vn.toancauxanh.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSetting is a Querydsl query type for Setting
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSetting extends EntityPathBase<Setting> {

    private static final long serialVersionUID = -455423591L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QSetting setting = new QSetting("setting");

    public final QModel _super;

    public final BooleanPath canBoQuanLy = createBoolean("canBoQuanLy");

    public final BooleanPath dacDiemNhanDang = createBoolean("dacDiemNhanDang");

    //inherited
    public final BooleanPath daXoa;

    public final NumberPath<Long> dem = createNumber("dem", Long.class);

    public final BooleanPath diaChiHienNay = createBoolean("diaChiHienNay");

    public final BooleanPath diaChiHienNayHuyen = createBoolean("diaChiHienNayHuyen");

    public final BooleanPath diaChiHienNayTinh = createBoolean("diaChiHienNayTinh");

    public final BooleanPath diaChiHienNayToDanPho = createBoolean("diaChiHienNayToDanPho");

    public final BooleanPath diaChiHienNayXa = createBoolean("diaChiHienNayXa");

    public final BooleanPath diaChiThuongTru = createBoolean("diaChiThuongTru");

    public final BooleanPath diaChiThuongTruHuyen = createBoolean("diaChiThuongTruHuyen");

    public final BooleanPath diaChiThuongTruTinh = createBoolean("diaChiThuongTruTinh");

    public final BooleanPath diaChiThuongTruToDanPho = createBoolean("diaChiThuongTruToDanPho");

    public final BooleanPath diaChiThuongTruXa = createBoolean("diaChiThuongTruXa");

    public final BooleanPath donViCanBoQuanLy = createBoolean("donViCanBoQuanLy");

    public final BooleanPath email = createBoolean("email");

    //inherited
    public final NumberPath<Long> id;

    public final BooleanPath ngayCapCMND = createBoolean("ngayCapCMND");

    public final NumberPath<Integer> ngayQuanLySauKhiRaTrungTam = createNumber("ngayQuanLySauKhiRaTrungTam", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    public final BooleanPath ngheNghiep = createBoolean("ngheNghiep");

    // inherited
    public final QNhanVien nguoiSua;

    // inherited
    public final QNhanVien nguoiTao;

    public final BooleanPath noiCapCMND = createBoolean("noiCapCMND");

    public final BooleanPath soCMND = createBoolean("soCMND");

    public final BooleanPath soDienThoai = createBoolean("soDienThoai");

    public final BooleanPath soDinhDanh = createBoolean("soDinhDanh");

    public final BooleanPath soDTCanBoQuanLy = createBoolean("soDTCanBoQuanLy");

    public final BooleanPath tenKhac = createBoolean("tenKhac");

    public final NumberPath<Integer> thangQuanLySauCai = createNumber("thangQuanLySauCai", Integer.class);

    public final NumberPath<Integer> thangQuanLySauViPham = createNumber("thangQuanLySauViPham", Integer.class);

    public final BooleanPath thanhPhanDoiTuong = createBoolean("thanhPhanDoiTuong");

    public final BooleanPath tinhTrangViecLam = createBoolean("tinhTrangViecLam");

    //inherited
    public final StringPath trangThai;

    public final BooleanPath trinhDoHocVan = createBoolean("trinhDoHocVan");

    public final NumberPath<Integer> widthMedium = createNumber("widthMedium", Integer.class);

    public final NumberPath<Integer> widthSmall = createNumber("widthSmall", Integer.class);

    public QSetting(String variable) {
        this(Setting.class, forVariable(variable), INITS);
    }

    public QSetting(Path<? extends Setting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSetting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSetting(PathMetadata metadata, PathInits inits) {
        this(Setting.class, metadata, inits);
    }

    public QSetting(Class<? extends Setting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
    }

}

