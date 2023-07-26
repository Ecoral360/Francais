(##include-once "ribbit:r4rs-tc.scm")
(define-type FrancaisLangBool)
(define-type FrancaisLangTableau)
(define-type FrancaisLangTexte)
(define-type FrancaisLangEntier)
(define-type FrancaisLangDecimal)
(define-type FrancaisLangModule)
(define-type FrancaisLangObj)

(define (TypeDe o)
  (let ((type (assv (##field2 o) data-types)))
    (if type
      (cadr type)
      'FrancaisLangObj)))

(define Vrai #t)
(define Faux #f)

