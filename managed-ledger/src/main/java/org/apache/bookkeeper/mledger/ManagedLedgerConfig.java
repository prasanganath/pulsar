/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.bookkeeper.mledger;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import java.time.Clock;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.apache.bookkeeper.client.api.DigestType;

import org.apache.bookkeeper.mledger.impl.NullLedgerOffloader;

/**
 * Configuration class for a ManagedLedger.
 */
@Beta
public class ManagedLedgerConfig {

    private boolean createIfMissing = true;
    private int maxUnackedRangesToPersist = 10000;
    private int maxUnackedRangesToPersistInZk = 1000;
    private int maxEntriesPerLedger = 50000;
    private int maxSizePerLedgerMb = 100;
    private int minimumRolloverTimeMs = 0;
    private long maximumRolloverTimeMs = TimeUnit.HOURS.toMillis(4);
    private int ensembleSize = 3;
    private int writeQuorumSize = 2;
    private int ackQuorumSize = 2;
    private int metadataEnsembleSize = 3;
    private int metadataWriteQuorumSize = 2;
    private int metadataAckQuorumSize = 2;
    private int metadataMaxEntriesPerLedger = 50000;
    private int ledgerRolloverTimeout = 4 * 3600;
    private double throttleMarkDelete = 0;
    private long retentionTimeMs = 0;
    private long retentionSizeInMB = 0;
    private boolean autoSkipNonRecoverableData;
    private long offloadLedgerDeletionLagMs = TimeUnit.HOURS.toMillis(4);
    private long offloadAutoTriggerSizeThresholdBytes = -1;
    private long metadataOperationsTimeoutSeconds = 60;
    private long readEntryTimeoutSeconds = 120;

    private DigestType digestType = DigestType.CRC32C;
    private byte[] password = "".getBytes(Charsets.UTF_8);
    private LedgerOffloader ledgerOffloader = NullLedgerOffloader.INSTANCE;
    private Clock clock = Clock.systemUTC();

    public boolean isCreateIfMissing() {
        return createIfMissing;
    }

    public ManagedLedgerConfig setCreateIfMissing(boolean createIfMissing) {
        this.createIfMissing = createIfMissing;
        return this;
    }

    /**
     * @return the maxEntriesPerLedger
     */
    public int getMaxEntriesPerLedger() {
        return maxEntriesPerLedger;
    }

    /**
     * @param maxEntriesPerLedger
     *            the maxEntriesPerLedger to set
     */
    public ManagedLedgerConfig setMaxEntriesPerLedger(int maxEntriesPerLedger) {
        this.maxEntriesPerLedger = maxEntriesPerLedger;
        return this;
    }

    /**
     * @return the maxSizePerLedgerMb
     */
    public int getMaxSizePerLedgerMb() {
        return maxSizePerLedgerMb;
    }

    /**
     * @param maxSizePerLedgerMb
     *            the maxSizePerLedgerMb to set
     */
    public ManagedLedgerConfig setMaxSizePerLedgerMb(int maxSizePerLedgerMb) {
        this.maxSizePerLedgerMb = maxSizePerLedgerMb;
        return this;
    }

    /**
     * @return the minimum rollover time
     */
    public int getMinimumRolloverTimeMs() {
        return minimumRolloverTimeMs;
    }

    /**
     * Set the minimum rollover time for ledgers in this managed ledger.
     *
     * <p/>If this time is > 0, a ledger will not be rolled over more frequently than the specified time, even if it has
     * reached the maximum number of entries or maximum size. This parameter can be used to reduce the amount of
     * rollovers on managed ledger with high write throughput.
     *
     * @param minimumRolloverTime
     *            the minimum rollover time
     * @param unit
     *            the time unit
     */
    public void setMinimumRolloverTime(int minimumRolloverTime, TimeUnit unit) {
        this.minimumRolloverTimeMs = (int) unit.toMillis(minimumRolloverTime);
        checkArgument(maximumRolloverTimeMs >= minimumRolloverTimeMs,
                "Minimum rollover time needs to be less than maximum rollover time");
    }

    /**
     * @return the maximum rollover time.
     */
    public long getMaximumRolloverTimeMs() {
        return maximumRolloverTimeMs;
    }

    /**
     * Set the maximum rollover time for ledgers in this managed ledger.
     *
     * <p/>If the ledger is not rolled over until this time, even if it has not reached the number of entry or size
     * limit, this setting will trigger rollover. This parameter can be used for topics with low request rate to force
     * rollover, so recovery failure does not have to go far back.
     *
     * @param maximumRolloverTime
     *            the maximum rollover time
     * @param unit
     *            the time unit
     */
    public void setMaximumRolloverTime(int maximumRolloverTime, TimeUnit unit) {
        this.maximumRolloverTimeMs = unit.toMillis(maximumRolloverTime);
        checkArgument(maximumRolloverTimeMs >= minimumRolloverTimeMs,
                "Maximum rollover time needs to be greater than minimum rollover time");
    }

    /**
     * @return the ensembleSize
     */
    public int getEnsembleSize() {
        return ensembleSize;
    }

    /**
     * @param ensembleSize
     *            the ensembleSize to set
     */
    public ManagedLedgerConfig setEnsembleSize(int ensembleSize) {
        this.ensembleSize = ensembleSize;
        return this;
    }

    /**
     * @return the ackQuorumSize
     */
    public int getAckQuorumSize() {
        return ackQuorumSize;
    }

    /**
     * @return the writeQuorumSize
     */
    public int getWriteQuorumSize() {
        return writeQuorumSize;
    }

    /**
     * @param writeQuorumSize
     *            the writeQuorumSize to set
     */
    public ManagedLedgerConfig setWriteQuorumSize(int writeQuorumSize) {
        this.writeQuorumSize = writeQuorumSize;
        return this;
    }

    /**
     * @param ackQuorumSize
     *            the ackQuorumSize to set
     */
    public ManagedLedgerConfig setAckQuorumSize(int ackQuorumSize) {
        this.ackQuorumSize = ackQuorumSize;
        return this;
    }

    /**
     * @return the digestType
     */
    public DigestType getDigestType() {
        return digestType;
    }

    /**
     * @param digestType
     *            the digestType to set
     */
    public ManagedLedgerConfig setDigestType(DigestType digestType) {
        this.digestType = digestType;
        return this;
    }

    /**
     * @return the password
     */
    public byte[] getPassword() {
        return Arrays.copyOf(password, password.length);
    }

    /**
     * @param password
     *            the password to set
     */
    public ManagedLedgerConfig setPassword(String password) {
        this.password = password.getBytes(Charsets.UTF_8);
        return this;
    }

    /**
     * @return the metadataEnsemblesize
     */
    public int getMetadataEnsemblesize() {
        return metadataEnsembleSize;
    }

    /**
     * @param metadataEnsembleSize
     *            the metadataEnsembleSize to set
     */
    public ManagedLedgerConfig setMetadataEnsembleSize(int metadataEnsembleSize) {
        this.metadataEnsembleSize = metadataEnsembleSize;
        return this;
    }

    /**
     * @return the metadataAckQuorumSize
     */
    public int getMetadataAckQuorumSize() {
        return metadataAckQuorumSize;
    }

    /**
     * @return the metadataWriteQuorumSize
     */
    public int getMetadataWriteQuorumSize() {
        return metadataWriteQuorumSize;
    }

    /**
     * @param metadataAckQuorumSize
     *            the metadataAckQuorumSize to set
     */
    public ManagedLedgerConfig setMetadataAckQuorumSize(int metadataAckQuorumSize) {
        this.metadataAckQuorumSize = metadataAckQuorumSize;
        return this;
    }

    /**
     * @param metadataWriteQuorumSize
     *            the metadataWriteQuorumSize to set
     */
    public ManagedLedgerConfig setMetadataWriteQuorumSize(int metadataWriteQuorumSize) {
        this.metadataWriteQuorumSize = metadataWriteQuorumSize;
        return this;
    }

    /**
     * @return the metadataMaxEntriesPerLedger
     */
    public int getMetadataMaxEntriesPerLedger() {
        return metadataMaxEntriesPerLedger;
    }

    /**
     * @param metadataMaxEntriesPerLedger
     *            the metadataMaxEntriesPerLedger to set
     */
    public ManagedLedgerConfig setMetadataMaxEntriesPerLedger(int metadataMaxEntriesPerLedger) {
        this.metadataMaxEntriesPerLedger = metadataMaxEntriesPerLedger;
        return this;
    }

    /**
     * @return the ledgerRolloverTimeout
     */
    public int getLedgerRolloverTimeout() {
        return ledgerRolloverTimeout;
    }

    /**
     * @param ledgerRolloverTimeout
     *            the ledgerRolloverTimeout to set
     */
    public ManagedLedgerConfig setLedgerRolloverTimeout(int ledgerRolloverTimeout) {
        this.ledgerRolloverTimeout = ledgerRolloverTimeout;
        return this;
    }

    /**
     * @return the throttling rate limit for mark-delete calls
     */
    public double getThrottleMarkDelete() {
        return throttleMarkDelete;
    }

    /**
     * Set the rate limiter on how many mark-delete calls per second are allowed. If the value is set to 0, the rate
     * limiter is disabled. Default is 0.
     *
     * @param throttleMarkDelete
     *            the max number of mark-delete calls allowed per second
     */
    public ManagedLedgerConfig setThrottleMarkDelete(double throttleMarkDelete) {
        checkArgument(throttleMarkDelete >= 0.0);
        this.throttleMarkDelete = throttleMarkDelete;
        return this;
    }

    /**
     * Set the retention time for the ManagedLedger
     * <p>
     * Retention time will prevent data from being deleted for at least the specified amount of time, even if no cursors
     * are created, or if all the cursors have marked the data for deletion.
     * <p>
     * A retention time of 0 (the default), will to have no time based retention.
     * <p>
     * Specifying a negative retention time will make the data to be retained indefinitely, based on the
     * {@link #setRetentionSizeInMB(long)} value.
     *
     * @param retentionTime
     *            duration for which messages should be retained
     * @param unit
     *            time unit for retention time
     */
    public ManagedLedgerConfig setRetentionTime(int retentionTime, TimeUnit unit) {
        this.retentionTimeMs = unit.toMillis(retentionTime);
        return this;
    }

    /**
     * @return duration for which messages are retained
     *
     */
    public long getRetentionTimeMillis() {
        return retentionTimeMs;
    }

    /**
     * The retention size is used to set a maximum retention size quota on the ManagedLedger.
     * <p>
     * This setting works in conjuction with {@link #setRetentionSizeInMB(long)} and places a max size for retention,
     * after which the data is deleted.
     * <p>
     * A retention size of 0, will make data to be deleted immediately.
     * <p>
     * A retention size of -1, means to have an unlimited retention size.
     *
     * @param retentionSizeInMB
     *            quota for message retention
     */
    public ManagedLedgerConfig setRetentionSizeInMB(long retentionSizeInMB) {
        this.retentionSizeInMB = retentionSizeInMB;
        return this;
    }

    /**
     * @return quota for message retention
     *
     */
    public long getRetentionSizeInMB() {
        return retentionSizeInMB;
    }

    /**
     * When a ledger is offloaded from bookkeeper storage to longterm storage, the bookkeeper ledger
     * is not deleted immediately. Instead we wait for a grace period before deleting from bookkeeper.
     * The offloadLedgerDeleteLag sets this grace period.
     *
     * @param lagTime period to wait before deleting offloaded ledgers from bookkeeper
     * @param unit timeunit for lagTime
     */
    public ManagedLedgerConfig setOffloadLedgerDeletionLag(long lagTime, TimeUnit unit) {
        this.offloadLedgerDeletionLagMs = unit.toMillis(lagTime);
        return this;
    }

    /**
     * Number of milliseconds before an offloaded ledger will be deleted from bookkeeper.
     *
     * @return the offload ledger deletion lag time in milliseconds
     */
    public long getOffloadLedgerDeletionLagMillis() {
        return offloadLedgerDeletionLagMs;
    }

    /**
     * Size, in bytes, at which the managed ledger will start to automatically offload ledgers to longterm storage.
     * A negative value disables autotriggering. A threshold of 0 offloads data as soon as possible.
     * Offloading will not occur if no offloader has been set {@link #setLedgerOffloader(LedgerOffloader)}.
     * Automatical offloading occurs when the ledger is rolled, and the ledgers up to that point exceed the threshold.
     *
     * @param threshold Threshold in bytes at which offload is automatically triggered
     */
    public ManagedLedgerConfig setOffloadAutoTriggerSizeThresholdBytes(long threshold) {
        this.offloadAutoTriggerSizeThresholdBytes = threshold;
        return this;
    }

    /**
     * Size, in bytes, at which offloading will automatically be triggered for this managed ledger.
     * @return the trigger threshold, in bytes
     */
    public long getOffloadAutoTriggerSizeThresholdBytes() {
        return this.offloadAutoTriggerSizeThresholdBytes;
    }

    /**
     * Skip reading non-recoverable/unreadable data-ledger under managed-ledger's list. It helps when data-ledgers gets
     * corrupted at bookkeeper and managed-cursor is stuck at that ledger.
     */
    public boolean isAutoSkipNonRecoverableData() {
        return autoSkipNonRecoverableData;
    }

    public void setAutoSkipNonRecoverableData(boolean skipNonRecoverableData) {
        this.autoSkipNonRecoverableData = skipNonRecoverableData;
    }

    /**
     * @return max unacked message ranges that will be persisted and recovered.
     *
     */
    public int getMaxUnackedRangesToPersist() {
        return maxUnackedRangesToPersist;
    }

    /**
     * @param maxUnackedRangesToPersist
     *            max unacked message ranges that will be persisted and receverd.
     */
    public ManagedLedgerConfig setMaxUnackedRangesToPersist(int maxUnackedRangesToPersist) {
        this.maxUnackedRangesToPersist = maxUnackedRangesToPersist;
        return this;
    }

    /**
     * @return max unacked message ranges up to which it can store in Zookeeper
     *
     */
    public int getMaxUnackedRangesToPersistInZk() {
        return maxUnackedRangesToPersistInZk;
    }

    public void setMaxUnackedRangesToPersistInZk(int maxUnackedRangesToPersistInZk) {
        this.maxUnackedRangesToPersistInZk = maxUnackedRangesToPersistInZk;
    }

    /**
     * Get ledger offloader which will be used to offload ledgers to longterm storage.
     *
     * The default offloader throws an exception on any attempt to offload.
     *
     * @return a ledger offloader
     */
    public LedgerOffloader getLedgerOffloader() {
        return ledgerOffloader;
    }

    /**
     * Set ledger offloader to use for offloading ledgers to longterm storage.
     *
     * @param offloader the ledger offloader to use
     */
    public ManagedLedgerConfig setLedgerOffloader(LedgerOffloader offloader) {
        this.ledgerOffloader = offloader;
        return this;
    }

    /**
     * Get clock to use to time operations
     *
     * @return a clock
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * Set clock to use for time operations
     *
     * @param clock the clock to use
     */
    public ManagedLedgerConfig setClock(Clock clock) {
        this.clock = clock;
        return this;
    }

    /**
     * 
     * Ledger-Op (Create/Delete) timeout
     * 
     * @return
     */
    public long getMetadataOperationsTimeoutSeconds() {
        return metadataOperationsTimeoutSeconds;
    }

    /**
     * Ledger-Op (Create/Delete) timeout after which callback will be completed with failure
     * 
     * @param metadataOperationsTimeoutSeconds
     */
    public ManagedLedgerConfig setMetadataOperationsTimeoutSeconds(long metadataOperationsTimeoutSeconds) {
        this.metadataOperationsTimeoutSeconds = metadataOperationsTimeoutSeconds;
        return this;
    }
    
    /**
     * Ledger read-entry timeout
     * 
     * @return
     */
    public long getReadEntryTimeoutSeconds() {
        return readEntryTimeoutSeconds;
    }

    /**
     * Ledger read entry timeout after which callback will be completed with failure. (disable timeout by setting
     * readTimeoutSeconds <= 0)
     * 
     * @param readTimeoutSeconds
     * @return
     */
    public ManagedLedgerConfig setReadEntryTimeoutSeconds(long readEntryTimeoutSeconds) {
        this.readEntryTimeoutSeconds = readEntryTimeoutSeconds;
        return this;
    }
}
