import fetch from "../util/fetch-fill";
import URI from "urijs";
import { resolve } from "url";
// /records endpoint
window.path = "http://localhost:3000/records";

// Your retrieve function plus any additional functions go here ...
const limit = 10;

// Additional helpers
function isPrimaryColors(color) {
    const primaryColors = ["red", "blue", "yellow"]; // There should be an API to return array of primary colors ...

    return primaryColors.indexOf(color) >= 0 ? true : false;
}
function totalPage() {
    const totalItems = 500; // There should be an API to return number of total items ...

    return (totalItems % limit == 0) ? totalItems / limit : (totalItems / limit + 1);
}
function genUrl(page, colors) {
    const offset = (page - 1) * limit;
    
    let url = '';
    if ( !!colors && Array.isArray(colors) && colors.length > 0 ) {
        url = URI(window.path).search({offset: offset, limit: limit, 'color[]': colors});
    } else {
        url = URI(window.path).search({offset: offset, limit: limit});
    }

    return url;
}

function retrieve(params) {    
    return new Promise((resolve, reject) => {
        const requestOptions = {
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json',
            },
        };
        
        let {page, colors} = !!params ? params : {page: 1,};
        if ( !page || page <= 0 ) {
            page = 1;
        }
        if( colors && colors.length <= 0 ) {
            colors = undefined;
        }

        const url = genUrl(page, colors);
        
        fetch(url, requestOptions)
            .then(response => {
                if (!!response.ok) {
                    return response.json();
                }
                console.log(response.statusText);
                
                reject();
            })
            .then(json => {
                let closedPrimaryCount = 0;
                let open = [];
                const ids = json.map(item => {
                    if (item.disposition === 'closed') {
                        if (isPrimaryColors(item.color)) {
                            closedPrimaryCount += 1;
                        }
                    } else {
                        open.push({id: item.id, color: item.color, disposition: item.disposition, isPrimary: isPrimaryColors(item.color)});
                    }
                    return item.id;
                });

                const result = {
                    previousPage: page === 1 ? null : page - 1,
                    nextPage: (page + 1 < totalPage() && json.length === limit) ? page + 1 : null,
                    ids,
                    open: open,
                    closedPrimaryCount,
                };

                resolve(result);

            }).catch(error => {
                reject(error);
            });
    })
    .catch(err => {
        console.log(err);
    });
}

export default retrieve;
