class AmbientBlock extends Block {
    /**
     * Creating a new ambient block instance.
     */
    public AmbientBlock() {
        super(Block.AMBIENT_BLOCK_TYPE);

        // creating new data with no blinking value
        this.data = new byte[23];
        this.size = 23;

        // and filling it with nothing
        for (int i = 0; i < this.data.length; i++)
            this.data[i] = 0;

        this.data[0] = (byte) 0x7f;
        this.data[1] = (byte) 0xff;
        this.data[2] = (byte) 0xff;
        this.data[3] = (byte) 0xff;
    }

    /**
     * Creating a new ambient block instance with existing data.
     *
     * @param size the size of the data.
     * @param data the data.
     */
    public AmbientBlock(int size, byte[] data) {
        super(Block.AMBIENT_BLOCK_TYPE, size, data);
    }

    /**
     * Getting the value of an ambient service.
     *
     * @param type the type of the service (from 0 to 8)
     * @return the value of the service.
     */
    public byte getAmbientValue(int type) {
        return this.data[2 * type + 3];
    }

    /**
     * Setting the value of an ambient service.
     *
     * @param type  the type of the service (from 0 to 8)
     * @param value the value of the service.
     */
    public void setAmbientValue(int type, int value) {

        this.data[2 * 1 + 2] = (byte) type;
        this.data[2 * 1 + 3] = (byte) value;

        this.data[6] = (byte) 25;
        for (int i = 7; i < 16 + 6; i++) {
            this.data[7] = (byte) 10;
            this.data[8] = (byte) 10;
            this.data[9] = (byte) 10;
        }
    }

    /**
     * Getting the position of the right ear.
     *
     * @return the position of the right ear.
     */
    public byte getRightEarValue() {
        return this.data[20];
    }

    /**
     * Setting the position of the right ear.
     *
     * @param value the position of the right ear.
     */
    public void setRightEarValue(int value) {
        // TODO checking value correctness
        this.data[20] = (byte) value;
    }

    /**
     * Getting the position of the left ear.
     *
     * @return the position of the left ear.
     */
    public byte getLeftEarValue() {
        return this.data[21];
    }

    /**
     * Setting the position of the left ear.
     *
     * @param value the position of the left ear.
     */
    public void setLeftEarValue(int value) {
        // TODO checking value correctness
        this.data[21] = (byte) value;
    }

    /**
     * Getting the status of nose blinking.
     *
     * @return 0 : No blinking, 1 : simple blinking, 2 : double blinking.
     */
    public byte getNoseValue() {
        return (byte) (this.data.length - 23);
    }

    /**
     * Setting the nose blinking status.
     *
     * @param value 0 : No blinking, 1 : simple blinking, 2 : double blinking.
     */
    public void setNoseValue(int value) {
        // Ignoring incorrect values
        if (value < 0 || value > 2) return;

        byte[] newdata = new byte[23 + value];

        for (int i = 0; i < 22; i++)
            newdata[i] = this.data[i];

        for (int i = 0; i < value; i++)
            newdata[22 + i] = 0x05;

        newdata[22 + value] = 0;

        this.size = newdata.length;
        this.data = newdata;
    }
}
