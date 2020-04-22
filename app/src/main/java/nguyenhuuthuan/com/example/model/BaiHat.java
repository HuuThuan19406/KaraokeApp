package nguyenhuuthuan.com.example.model;

public class BaiHat {
    public static enum CamXuc {
        dislike(0),
        like(1);

        int msg;
        private CamXuc (int msg) {
            this.msg = msg;
        }
        public int getDescription()
        {
            return this.msg;
        }

    }
    private String ma,ten,caSi;
    private CamXuc camXuc;

    public BaiHat() {
    }

    public BaiHat(String ma, String ten, String caSi, CamXuc camXuc) {
        this.ma = ma;
        this.ten = ten;
        this.caSi = caSi;
        this.camXuc = camXuc;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getCaSi() {
        return caSi;
    }

    public void setCaSi(String caSi) {
        this.caSi = caSi;
    }

    public CamXuc getCamXuc() {
        return camXuc;
    }

    public void setCamXuc(CamXuc camXuc) {
        this.camXuc = camXuc;
    }

}
