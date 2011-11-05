application.log "ping, params=" + params

def packet = new Packet();
def message = new AmbientBlock()
//message.leftEarValue = 4
//message.rightEarValue = 4
//message.setAmbientValue 14, 3
def mes = new MessageBlock(139)
mes.addPlayLocalChoreographyCommand "red"
packet.addBlock new PingIntervalBlock(10)
if(params.sd == '3')
    packet.addBlock mes
//packet.addBlock(message)
def packetGeneratePacket = packet.generatePacket()
application.log "packet" + packetGeneratePacket
response.contentLength = packetGeneratePacket.length
sout.write packetGeneratePacket
response.status = 200
