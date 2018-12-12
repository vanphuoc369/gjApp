package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBaiViet is a Querydsl query type for BaiViet
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBaiViet extends EntityPathBase<BaiViet> {

    private static final long serialVersionUID = -1217284245L;

    private static final PathInits INITS = new PathInits("*", "author.*.*.*.*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QBaiViet baiViet = new QBaiViet("baiViet");

    public final QAsset _super;

    public final StringPath alias = createString("alias");

    public final vn.toancauxanh.model.QNhanVien author;

    public final QImage avatarImage;

    public final ListPath<Category, QCategory> categories = this.<Category, QCategory>createList("categories", Category.class, QCategory.class, PathInits.DIRECT2);

    //inherited
    public final BooleanPath ckEditorPopup;

    public final StringPath content = createString("content");

    public final NumberPath<Integer> countImage = createNumber("countImage", Integer.class);

    //inherited
    public final BooleanPath daXoa;

    public final StringPath description = createString("description");

    public final ListPath<FileEntry, QFileEntry> fileEntries = this.<FileEntry, QFileEntry>createList("fileEntries", FileEntry.class, QFileEntry.class, PathInits.DIRECT2);

    public final StringPath friendlyUrl = createString("friendlyUrl");

    //inherited
    public final NumberPath<Long> id;

    public final StringPath metaDescription = createString("metaDescription");

    public final StringPath metaKeyword = createString("metaKeyword");

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final BooleanPath noiBat = createBoolean("noiBat");

    public final DateTimePath<java.util.Date> publishBeginTime = createDateTime("publishBeginTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> publishEndTime = createDateTime("publishEndTime", java.util.Date.class);

    public final BooleanPath publishStatus = createBoolean("publishStatus");

    public final NumberPath<Integer> readCount = createNumber("readCount", Integer.class);

    public final NumberPath<Integer> soThuTu = createNumber("soThuTu", Integer.class);

    public final StringPath subTitle = createString("subTitle");

    public final BooleanPath tieuDiem = createBoolean("tieuDiem");

    public final StringPath title = createString("title");

    //inherited
    public final StringPath trangThai;

    public final StringPath trangThaiHienThi = createString("trangThaiHienThi");

    //inherited
    public final StringPath trangThaiSoan;

    public QBaiViet(String variable) {
        this(BaiViet.class, forVariable(variable), INITS);
    }

    public QBaiViet(Path<? extends BaiViet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBaiViet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBaiViet(PathMetadata metadata, PathInits inits) {
        this(BaiViet.class, metadata, inits);
    }

    public QBaiViet(Class<? extends BaiViet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QAsset(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new vn.toancauxanh.model.QNhanVien(forProperty("author"), inits.get("author")) : null;
        this.avatarImage = inits.isInitialized("avatarImage") ? new QImage(forProperty("avatarImage"), inits.get("avatarImage")) : null;
        this.ckEditorPopup = _super.ckEditorPopup;
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
        this.trangThaiSoan = _super.trangThaiSoan;
    }

}

