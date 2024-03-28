import {post} from "../api";

export async function createShipment(body) {
    return await post("shipment", body);
}