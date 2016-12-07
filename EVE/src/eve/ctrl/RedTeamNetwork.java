package eve.ctrl;

import eve.model.attack.Attack;
import eve.model.event.Source;
import eve.model.event.Target;
import eve.model.infra.Host;
import eve.var.Utils;
import eve.wuti.jpa.DAO;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jesus on 12/1/16.
 */
class RedTeamNetwork {
    private List<Connection> connections = new LinkedList<>();

    private Host unknownHost;


    public RedTeamNetwork() {
        unknownHost = new Host("Unknown", null, "", "", null, 400, 400);

        List<Attack> attacks = DAO.get().getList(Attack.class);
        for (Attack attack : attacks) newEvent(attack, false);
    }

    public void newEvent(Attack attack, boolean isUpdate) {
        if (attack == null) return;
        if (isUpdate) return;

        Source source = attack.getSource();
        Target target = attack.getTarget();

        for (Connection connection : connections)
            if (connection.origin.equals(source.getHost()) && connection.destination.equals(target.getHost())) {
                connection.addAttack(attack);
                return;
            }

        Host origin, destination;
        if (source != null && source.getHost() != null)
            origin = source.getHost();
        else
            origin = unknownHost;

        if (target != null && target.getHost() != null)
            destination = target.getHost();
        else
            destination = unknownHost;

        Connection connection = new Connection(origin, destination, attack);
        if (!connections.contains(connection)) connections.add(connection);
    }


    public String toJSON() {

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append("\"nodes\":[");
        List<Host> visitedHosts = new LinkedList<>();
        for (Connection connection : connections) {
            if (!visitedHosts.contains(connection.origin)) {
                sb.append(connection.originToJSON()).append(",");
                visitedHosts.add(connection.origin);
            }
            if (!visitedHosts.contains(connection.destination)) {
                sb.append(connection.destinationToJSON()).append(",");
                visitedHosts.add(connection.destination);
            }
        }
        if (visitedHosts.size() > 0)
            sb.deleteCharAt(sb.length() - 1); // delete last comma
        sb.append("],");


        sb.append("\"edges\":[");
        for (Connection connection : connections)
            sb.append(connection.toJSON()).append(",");
        if (connections.size() > 0)
            sb.deleteCharAt(sb.length() - 1); // delete last comma
        sb.append("],");

        sb.append("\"attacks\":[");
        for (Connection connection : connections) {
            for (Attack attack : connection.attacks) {
                sb.append(attack.toRedTeamJSON((int) unknownHost.getId())).append(",");
            }
        }
        if (sb.charAt(sb.length() - 1) == ',')
            sb.deleteCharAt(sb.length() - 1); // delete last comma
        sb.append("]");

        sb.append("}");

        System.out.println(sb.toString());
        return sb.toString();
    }


    private int lastConnectionID = 0;

    private class Connection {
        int id;
        Host origin;
        Host destination;
        String ipv4Origin, ipv4Destination, ipv6Origin, ipv6Destination;
        LinkedList<Attack> attacks = new LinkedList<>();

        private Connection(Host origin, Host destination, Attack attack) {
            this.id = lastConnectionID++;
            this.origin = origin;
            this.destination = destination;
            addAttack(attack);
        }

        private void addAttack(Attack attack) {
            attacks.add(attack);
            extractInfo(attack);
        }

        void extractInfo(Attack attack) {
            if (attack == null) return;
            if (attack.getSource() != null && Utils.hasInfo(attack.getSource().getIPV4())) ipv4Origin = attack.getSource().getIPV4();
            if (attack.getTarget() != null && Utils.hasInfo(attack.getTarget().getIPV4())) ipv4Destination = attack.getTarget().getIPV4();
            if (attack.getSource() != null && Utils.hasInfo(attack.getSource().getIPV6())) ipv6Origin = attack.getSource().getIPV6();
            if (attack.getTarget() != null && Utils.hasInfo(attack.getTarget().getIPV6())) ipv6Destination = attack.getTarget().getIPV6();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Connection that = (Connection) o;

            if (destination != null ? !destination.equals(that.destination) : that.destination != null) return false;
            if (origin != null ? !origin.equals(that.origin) : that.origin != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = origin != null ? origin.hashCode() : 0;
            result = 31 * result + (destination != null ? destination.hashCode() : 0);
            return result;
        }

        String toJSON() {
            return "{\"id\":" + id + ", \"originID\": \"" + (origin != null ? origin.id : "") + "\", \"destinationID\": \"" + (destination != null ? destination.id : "") + "\"}";
        }

        String originToJSON() {
            return "{\"id\":" + origin.id + ", \"IPV4\": \"" + (ipv4Origin != null ? ipv4Origin : "") + "\", \"IPV6\": \"" + (ipv6Origin != null ? ipv6Origin : "") + "\"}";
        }

        String destinationToJSON() {
            return "{\"id\":" + destination.id + ", \"IPV4\": \"" + (ipv4Destination != null ? ipv4Destination : "") + "\", \"IPV6\": \"" + (ipv6Destination != null ? ipv6Destination : "") + "\"}";
        }
    }
}
