application.log "ping, params=" + params

def packet = new Packet();
def message = new AmbientBlock()
//message.leftEarValue = Math.abs(new Random().nextInt()) % 12
//message.rightEarValue = Math.abs(new Random().nextInt()) % 12
message.setAmbientValue 7, 6
message.setNoseValue(2)
// we need that to be in a good state... MSG_IDLE state to move ears
def mes = new MessageBlock(0x7FFFFFFF)
mes.addPlayLocalChoreographyCommand "red"
packet.addBlock new PingIntervalBlock(10)
//if(params.sd == '3')
  // packet.addBlock mes
//packet.addBlock mes
packet.addBlock(message)
def packetGeneratePacket = packet.generatePacket()
application.log "packet" + packetGeneratePacket
response.contentLength = packetGeneratePacket.length
sout.write packetGeneratePacket
response.status = 200
