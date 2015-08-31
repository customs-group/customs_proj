package str_rec;

/**
 * Created by edwardlol on 15/8/31.
 */
public class Entry_info {
    private String entry_id;
    private String code_ts;
    private String origin_country;
    private String g_name;
    private String g_model;
    private String extracted_brand;
    private String discription;
    private String final_brand;

    public void setEntry_id (String entry_id) {
        this.entry_id = entry_id;
    }
    public void setCode_ts (String code_ts) {
        this.code_ts = code_ts;
    }
    public void setOrigin_country (String origin_country) {
        this.origin_country = origin_country;
    }
    public void setG_name (String g_name) {
        this.g_name = g_name;
    }
    public void setG_model (String g_model) {
        this.g_model = g_model;
    }
    public void setExtracted_brand (String extracted_brand) {
        this.extracted_brand = extracted_brand;
    }
    public void setDiscription (String discription) {
        this.discription = discription;
    }
    public void setFinal_brand (String final_brand) {
        this.final_brand = final_brand;
    }
    public String getEntry_id () {
        return this.entry_id;
    }
    public String getCode_ts () {
        return this.code_ts;
    }
    public String getOrigin_country () {
        return this.origin_country;
    }
    public String getG_name () {
        return this.g_name;
    }
    public String getG_model () {
        return this.g_model;
    }
    public String getExtracted_brand () {
        return this.extracted_brand;
    }
    public String getDiscription () {
        return this.discription;
    }
    public String getFinal_brand () {
        return this.final_brand;
    }
}
