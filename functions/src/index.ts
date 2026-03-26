/**
 * Cloud Functions for NTISocial/Afterlight
 * Handles party creation, joining, and cleanup operations
 */

import {onCall} from "firebase-functions/v2/https";
import {onSchedule} from "firebase-functions/v2/scheduler";
import * as logger from "firebase-functions/logger";
import * as admin from "firebase-admin";
import {setGlobalOptions} from "firebase-functions/v2";

admin.initializeApp();

// Set global options for cost control
setGlobalOptions({maxInstances: 10});

/**
 * Create a new party
 * Callable function that creates a party with the authenticated user as host
 */
export const createParty = onCall(async (request) => {
  // Verify authentication
  if (!request.auth) {
    throw new Error("User must be authenticated");
  }

  const {name, expiresAt} = request.data;

  // Validate input
  if (!name || typeof name !== "string") {
    throw new Error("Party name is required");
  }

  if (!expiresAt || typeof expiresAt !== "number") {
    throw new Error("Party expiration time is required");
  }

  const userId = request.auth.uid;
  const db = admin.firestore();

  try {
    // Create party document
    const partyRef = await db.collection("parties").add({
      name: name,
      hostUserId: userId,
      members: [userId],
      createdAt: admin.firestore.FieldValue.serverTimestamp(),
      expiresAt: new Date(expiresAt),
      isActive: true,
    });

    logger.info(`Party created: ${partyRef.id} by user ${userId}`);

    return {
      partyId: partyRef.id,
      success: true,
    };
  } catch (error) {
    logger.error("Error creating party:", error);
    throw new Error("Failed to create party");
  }
});

/**
 * Join an existing party
 * Callable function that adds the authenticated user to a party
 */
export const joinParty = onCall(async (request) => {
  if (!request.auth) {
    throw new Error("User must be authenticated");
  }

  const {partyId} = request.data;

  if (!partyId || typeof partyId !== "string") {
    throw new Error("Party ID is required");
  }

  const userId = request.auth.uid;
  const db = admin.firestore();

  try {
    const partyRef = db.collection("parties").doc(partyId);
    const partyDoc = await partyRef.get();

    if (!partyDoc.exists) {
      throw new Error("Party not found");
    }

    const partyData = partyDoc.data();

    // Check if party is still active
    if (!partyData?.isActive) {
      throw new Error("Party is no longer active");
    }

    // Check if user is already a member
    if (partyData?.members?.includes(userId)) {
      return {
        success: true,
        message: "Already a member",
      };
    }

    // Add user to members array
    await partyRef.update({
      members: admin.firestore.FieldValue.arrayUnion(userId),
    });

    logger.info(`User ${userId} joined party ${partyId}`);

    return {
      success: true,
      message: "Successfully joined party",
    };
  } catch (error) {
    logger.error("Error joining party:", error);
    throw new Error("Failed to join party");
  }
});

/**
 * Clean up expired parties
 * Scheduled function that runs daily to mark expired parties as inactive
 */
export const cleanupExpiredParties = onSchedule("every 24 hours", async () => {
  const db = admin.firestore();
  const now = admin.firestore.Timestamp.now();

  try {
    const expiredPartiesSnapshot = await db.collection("parties")
      .where("expiresAt", "<=", now.toDate())
      .where("isActive", "==", true)
      .get();

    const batch = db.batch();

    expiredPartiesSnapshot.forEach((doc) => {
      batch.update(doc.ref, {
        isActive: false,
        deactivatedAt: admin.firestore.FieldValue.serverTimestamp(),
      });
    });

    await batch.commit();

    logger.info(`Deactivated ${expiredPartiesSnapshot.size} expired parties`);
  } catch (error) {
    logger.error("Error cleaning up expired parties:", error);
  }
});

