/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendOrderNotification = functions.database.ref('/Orders/{orderId}')
    .onCreate((snapshot, context) => {
        const orderData = snapshot.val();
        const payload = {
            notification: {
                title: 'Đặt hàng thành công',
                body: `Đơn hàng của bạn với ID ${orderData.orderId} đã được đặt thành công.`
            }
        };

        const userId = orderData.iduser;
        return admin.database().ref(`/users/${userId}/fcmToken`).once('value').then(tokenSnapshot => {
            const token = tokenSnapshot.val();
            if (token) {
                return admin.messaging().sendToDevice(token, payload);
            } else {
                console.log('FCM token không tồn tại cho người dùng:', userId);
            }
        }).catch(error => {
            console.error('Lỗi khi lấy FCM token:', error);
        });
    });



// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
