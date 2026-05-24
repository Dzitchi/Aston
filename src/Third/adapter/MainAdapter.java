package Third.adapter;

public class MainAdapter {
    public static void main(String[] args) {
        MicroUsbDevice oldDevice = new MicroUsbDevice();
        UsbC adapter = new MicroUsbToUsbCAdapter(oldDevice);

        adapter.chargeWithUsbC();
    }
}
