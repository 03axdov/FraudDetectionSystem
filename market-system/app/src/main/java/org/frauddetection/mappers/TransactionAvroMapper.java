package org.frauddetection.mappers;

public final class TransactionAvroMapper {

    private TransactionAvroMapper() {}

    public static org.frauddetection.avro.AvroTransaction toAvro(
        org.frauddetection.models.events.Transaction tx
    ) {
        org.frauddetection.avro.AvroTransaction avroTx =
            new org.frauddetection.avro.AvroTransaction();

        avroTx.setTransactionId(tx.transactionId());
        avroTx.setTimestamp(tx.timestamp());
        avroTx.setAmount(tx.amount().doubleValue());
        avroTx.setCurrency(tx.currency());

        return avroTx;
    }
}