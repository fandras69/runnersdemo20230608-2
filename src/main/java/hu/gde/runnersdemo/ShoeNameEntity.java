package hu.gde.runnersdemo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

    @Entity
    public class ShoeNameEntity{

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long shoeNameId;
        private String shoeNameBrand;
        @JsonIgnore
        @OneToMany
        private List<RunnerEntity> shoeNameForRunners = new ArrayList<>();

        public long getShoeNameId() {
            return shoeNameId;
        }

        public void setShoeNameId(long shoeNameId) {
            this.shoeNameId = shoeNameId;
        }

        public String getShoeNameBrand() {
            return shoeNameBrand;
        }

        public void setShoeNameBrand(String shoeNameBrand) {
            this.shoeNameBrand = shoeNameBrand;
        }

        public List<RunnerEntity> getShoeForRunners() {
            return shoeNameForRunners;
        }

        public void setShoeForRunners(List<RunnerEntity> shoeForRunners) {
            this.shoeNameForRunners = shoeForRunners;
        }
    }

