========================================================
AnvyCart – Email Notification Payload Contract
========================================================

DTO USED:
---------
EmailRequest
{
  to        : String   (NOT BLANK)
  template  : String   (NOT BLANK)
  variables : Map<String, Object> (NOT NULL)
}

IMPORTANT:
----------
- "template" MUST match EmailTemplateResolver keys
- "variables" MUST contain ONLY fields used in HTML
- Extra fields are NOT allowed
- Missing fields will break template rendering

========================================================
REST BASED EMAIL REQUESTS
========================================================

--------------------------------------------------------
1. FORGOT PASSWORD EMAIL
--------------------------------------------------------

Template Key:
FORGOT_PASSWORD

HTML File:
email/forgot-password.html

Variables used in HTML:
- customerName
- otp

JSON Request:
{
  "to": "user@example.com",
  "template": "FORGOT_PASSWORD",
  "variables": {
    "customerName": "Nilesh",
    "otp": "123456"
  }
}

--------------------------------------------------------
2. EMAIL VERIFICATION
--------------------------------------------------------

Template Key:
EMAIL_VERIFICATION

HTML File:
email/email-verification.html

Variables used in HTML:
- customerName
- otp

JSON Request:
{
  "to": "user@example.com",
  "template": "EMAIL_VERIFICATION",
  "variables": {
    "customerName": "Nilesh",
    "otp": "654321"
  }
}

========================================================
EVENT BASED EMAIL REQUESTS (Kafka → Notification Service)
========================================================

NOTE:
-----
These are converted internally into EmailRequest
using template = eventType.name()

--------------------------------------------------------
3. ORDER PLACED
--------------------------------------------------------

Template Key:
ORDER_PLACED

HTML File:
email/order-placed.html

Variables used in HTML:
- customerName
- orderId
- productName
- amount

Event Data → EmailRequest.variables:
{
  "customerName": "Nilesh",
  "orderId": 101,
  "productName": "iPhone 15",
  "amount": 74999
}

--------------------------------------------------------
4. ORDER FAILED
--------------------------------------------------------

Template Key:
ORDER_FAILED

HTML File:
email/order-failed.html

Variables used in HTML:
- orderId
- failureReason

Event Data → EmailRequest.variables:
{
  "orderId": 101,
  "failureReason": "Payment authorization failed"
}

--------------------------------------------------------
5. ORDER CONFIRMED
--------------------------------------------------------

Template Key:
ORDER_CONFIRMED

HTML File:
email/order-confirmed.html

Variables used in HTML:
- orderId

Event Data → EmailRequest.variables:
{
  "orderId": 101
}

--------------------------------------------------------
6. ORDER SHIPPED
--------------------------------------------------------

Template Key:
ORDER_SHIPPED

HTML File:
email/order-shipped.html

Variables used in HTML:
- orderId
- trackingId
- expectedDelivery

Event Data → EmailRequest.variables:
{
  "orderId": 101,
  "trackingId": "TRK123456",
  "expectedDelivery": "2026-01-25"
}

--------------------------------------------------------
7. ORDER OUT FOR DELIVERY
--------------------------------------------------------

Template Key:
ORDER_OUT_FOR_DELIVERY

HTML File:
email/order-out-for-delivery.html

Variables used in HTML:
- orderId

Event Data → EmailRequest.variables:
{
  "orderId": 101
}

--------------------------------------------------------
8. ORDER DELIVERED
--------------------------------------------------------

Template Key:
ORDER_DELIVERED

HTML File:
email/order-delivered.html

Variables used in HTML:
- orderId

Event Data → EmailRequest.variables:
{
  "orderId": 101
}

--------------------------------------------------------
9. PAYMENT SUCCESS
--------------------------------------------------------

Template Key:
PAYMENT_SUCCESS

HTML File:
email/payment-success.html

Variables used in HTML:
- orderId
- amount

Event Data → EmailRequest.variables:
{
  "orderId": 101,
  "amount": 74999
}

--------------------------------------------------------
10. PAYMENT FAILED
--------------------------------------------------------

Template Key:
PAYMENT_FAILED

HTML File:
email/payment-failed.html

Variables used in HTML:
- orderId
- failureReason

Event Data → EmailRequest.variables:
{
  "orderId": 101,
  "failureReason": "Insufficient balance"
}

========================================================
FINAL RULES (VERY IMPORTANT)
========================================================

✔ EmailRequest.variables MUST NOT be null  
✔ Variable names MUST match Thymeleaf placeholders  
✔ Do NOT send unused fields  
✔ Do NOT send subject or HTML from producer  
✔ Template selection is done ONLY in Notification Service  

========================================================
END OF FILE
========================================================
