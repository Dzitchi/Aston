package Third.adapter;

class MicroUsbToUsbCAdapter implements UsbC {
    private final MicroUsbDevice device;

    public MicroUsbToUsbCAdapter(MicroUsbDevice device) {
        this.device = device;
    }

    public void chargeWithUsbC() {
        device.chargeWithMicroUsb();
    }
}

