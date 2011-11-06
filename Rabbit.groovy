import groovy.transform.ToString

@ToString
class Rabbit {

    private final DOUBLE_CLICK = '1'

    private final byte REBOOT = 0x9
    private final byte PING_INTERVAL = 0x3
    private final byte EARS_COLORS = 0x4

    enum Colors {
        NONE, RED, GREEN, YELLOW
    }

    def ears = ["left": 0, "right": 0]
    def leds = ["left": Colors.NONE, "middle": Colors.NONE, "right": Colors.NONE, "nose": Colors.NONE, "bottom": Colors.NONE]
    def interval = 0
    def hasToReboot = false

    def params

    def setLeds(color) {
        leds.each { it.value = color }
    }

    def doubleClicked() {
        return params.sd == DOUBLE_CLICK
    }

    def reboot() {
        hasToReboot = true
    }

    def send(response) {
        def fullPacket = buildPacket()

        def bytePacket = fullPacket as byte[]
        response.contentLength = bytePacket.length
        response.outputStream.write bytePacket
        response.status = 200

    }

    private def buildPacket() {
        def fullPacket = [0x7F]
        def pingIntervalPacket = [PING_INTERVAL, 0x0, 0x0, 0x1, interval]

        fullPacket += pingIntervalPacket

        def colorsPacket = [EARS_COLORS, 0x0, 0x0, 0x1C, 0x7F, 0xFF, 0xFF, 0xFF]

        colorsPacket += [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ears.left, ears.right, 0, leds.left.ordinal(), leds.middle.ordinal(), leds.right.ordinal(), 0, 0]

        fullPacket += colorsPacket

        if (hasToReboot)
            fullPacket = [0x7F, REBOOT, 0x0, 0x0, 0x1, 0x7F, 0xFF, 0xFF, 0xFF, 0xFF, 0xA]

        fullPacket += [0xFF, 0xA]
        return fullPacket
    }

}
