package com.wbl.weather.model;

import java.util.Date;
import java.util.List;

public class CityHourlyWeather {
    private String code;
    private Date updateTime;
    private String fxLink;
    private List<Hourly> hourly;
    private Refer refer;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public void setHourly(List<Hourly> hourly) {
        this.hourly = hourly;
    }

    public List<Hourly> getHourly() {
        return hourly;
    }

    public void setRefer(Refer refer) {
        this.refer = refer;
    }

    public Refer getRefer() {
        return refer;
    }

    public class Hourly {
        private String fxTime;
        private String temp;
        private String icon;
        private String text;
        private String wind360;
        private String windDir;
        private String windScale;
        private String windSpeed;
        private String humidity;
        private String pop;
        private String precip;
        private String pressure;
        private String cloud;
        private String dew;

        public String getFxTime() {
            return fxTime;
        }

        public void setFxTime(String fxTime) {
            this.fxTime = fxTime;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getTemp() {
            return temp;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setWind360(String wind360) {
            this.wind360 = wind360;
        }

        public String getWind360() {
            return wind360;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }

        public String getWindDir() {
            return windDir;
        }

        public String getWindScale() {
            return windScale;
        }

        public void setWindScale(String windScale) {
            this.windScale = windScale;
        }

        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getPop() {
            return pop;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }

        public String getPrecip() {
            return precip;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getPressure() {
            return pressure;
        }

        public void setCloud(String cloud) {
            this.cloud = cloud;
        }

        public String getCloud() {
            return cloud;
        }

        public void setDew(String dew) {
            this.dew = dew;
        }

        public String getDew() {
            return dew;
        }
    }

    public class Refer {
        private List<String> sources;
        private List<String> license;
        public void setSources(List<String> sources) {
            this.sources = sources;
        }
        public List<String> getSources() {
            return sources;
        }

        public void setLicense(List<String> license) {
            this.license = license;
        }
        public List<String> getLicense() {
            return license;
        }
    }
}
