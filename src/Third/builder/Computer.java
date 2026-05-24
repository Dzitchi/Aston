package Third.builder;

class Computer {
    private String hdd;
    private String ram;

    private Computer(Builder builder) {
        this.hdd = builder.hdd;
        this.ram = builder.ram;
    }

    public String getHdd() {
        return hdd;
    }

    public String getRam() {
        return ram;
    }

    public static class Builder {
        private String hdd;
        private String ram;

        public Builder setHdd(String hdd) {
            this.hdd = hdd;
            return this;
        }
        public Builder setRam(String ram) {
            this.ram = ram;
            return this;
        }
        public Computer build() {
            return new Computer(this);
        }
    }
}

