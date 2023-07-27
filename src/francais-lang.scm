(##include-once "ribbit:r4rs-tc.scm")

(define-type FrancaisLangBool)
(define-type FrancaisLangTableau)
(define-type FrancaisLangTexte)
(define-type FrancaisLangEntier)
(define-type FrancaisLangDecimal)
(define-type FrancaisLangModule)

(define (FrancaisLangObj? o)
  (and (##rib? o) (<= FrancaisLangBool-type (##field2 o) FrancaisLangModule-type)))

(define (FrancaisLang/Fonc/TypeDe o)
  (let ((type (assv (##field2 o) data-types)))
    (if type
      (cadr type)
      'FrancaisLangObj)))

(define (FrancaisLangObj->SchemeObj o)
  (let ((obj (if (not (FrancaisLangObj? o)) o (##field0 o))))
    (cond
      ((null? obj) '())

      ((or (FrancaisLangTableau? o) (pair? o))
        (cons (FrancaisLangObj->SchemeObj (car obj)) (FrancaisLangObj->SchemeObj (cdr obj))))

      (else obj))))

(define (FrancaisLangObj->string o)
  (define (FLO->string-aux obj)
    (cond
      ((pair? obj)
        (string-append "[" (string-concatenate (map FLO->string-aux obj) ", ") "]"))
      ((##eqv? obj #t)
        "VRAI")
      ((##eqv? obj #f)
        "FAUX")
      ((number? obj)
        (number->string obj))
      ((string? obj)
        obj)
      ((null? obj) "[]")))
  (FLO->string-aux (FrancaisLangObj->SchemeObj o)))


(define (TypeDe o) (FrancaisLang/Fonc/TypeDe o))

(define Vrai #t)
(define Faux #f)

