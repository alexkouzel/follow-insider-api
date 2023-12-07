export class StringUtils {

    static capitalize(val) {
        return val.charAt(0).toUpperCase() + val.slice(1).toLowerCase();
    }

    static formatNumber(val) {
        val = Math.abs(Number(val));
        if (val >= 1.0e+9) return (val / 1.0e+9).toFixed(2) + 'B';
        if (val >= 1.0e+6) return (val / 1.0e+6).toFixed(2) + 'M';
        if (val >= 1.0e+3) return (val / 1.0e+3).toFixed(2) + 'K';
        return val.toFixed(0);
    }

    static formatDate(val, options) {
        options = options || { month: 'short', day: 'numeric', year: 'numeric' };
        return new Date(val).toLocaleDateString('en-US', options);
    }

    static formatMoney(val, currency) {
        return currency + val.toFixed(2);
    }

}